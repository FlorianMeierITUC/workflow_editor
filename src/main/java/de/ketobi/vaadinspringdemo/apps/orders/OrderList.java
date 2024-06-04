package de.ketobi.vaadinspringdemo.apps.orders;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.orders.components.OrderDetailsDiv;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowTicketHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes.ORDER_WORKFLOW;

@Route(value = "orderlist", layout = MainLayout.class)
@PageTitle("My Orders")
public class OrderList extends VerticalLayout implements BeforeEnterObserver {
    private final OrderService orderService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowEntityService workflowEntityService;
    private Grid<Order> ordersGrid;
    private List<Order> orders = new ArrayList<>();
    private GridListDataView<Order> ordersView;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public OrderList(OrderService orderService, WorkflowNodeService workflowNodeService, WorkflowEntityService workflowEntityService) {
        this.orderService = orderService;
        this.workflowNodeService = workflowNodeService;
        this.workflowEntityService = workflowEntityService;
        this.ordersGrid = createOrdersGrid();
        add(new H3("My Orders"));
        add(new H4("These are the orders i created."));
        add(ordersGrid);
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            orders = orderService.getAllOrdersCreatedByTheCurrentUser();
            ordersView = ordersGrid.setItems(orders);
        }
    }

    private Grid<Order> createOrdersGrid() {
        Grid<Order> ordersGrid = new Grid<>(Order.class, false);
        ordersGrid.addComponentColumn(selectedOrder -> {
            Button detailsButton = new Button("Show details");
            detailsButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                dialog.add(new OrderDetailsDiv(selectedOrder, orderService));
                dialog.open();
            });
            return detailsButton;
        });
        ordersGrid.addColumn(order -> order.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        ordersGrid.addColumn(Order::getItem).setHeader("Item").setAutoWidth(true);
        ordersGrid.addColumn(Order::getSupplier).setHeader("Supplier").setAutoWidth(true);
        ordersGrid.addColumn(order -> order.getPrice().toString()).setHeader("Price").setAutoWidth(true);
        ordersGrid.addColumn(Order::getOrderNumber).setHeader("Order Number").setAutoWidth(true);
        ordersGrid.addColumn(order -> orderService.getCurrentNode(order).getTitle()).setHeader("Workflow node").setAutoWidth(true);
        ordersGrid.addColumn(order -> orderService.getCurrentResponsible(order).getName()).setHeader("Responsible").setAutoWidth(true);
        ordersGrid.addComponentColumn(selectedOrder -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Show workflow");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> nodes = workflowNodeService.getAll(ORDER_WORKFLOW.getId());
                dialog.add(new WorkflowView(nodes, orderService.getCurrentNode(selectedOrder)));
                dialog.open();
            });
            Button historyButton = new Button("Show history");
            historyButton.addClickListener(e -> {
                WorkflowTicketHistoryDialog dialog = new WorkflowTicketHistoryDialog(workflowEntityService.getWorkflowItemHistory(selectedOrder));
                dialog.open();
            });
            buttonDiv.add(editButton);
            buttonDiv.add(historyButton);
            return buttonDiv;
        });
        ordersGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        ordersGrid.setAllRowsVisible(true);
        return ordersGrid;
    }
}
