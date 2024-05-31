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
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowItemHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "orderlist", layout = MainLayout.class)
@PageTitle("My Orders")
public class OrderList extends VerticalLayout implements BeforeEnterObserver {
    private final OrderService orderService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowItemService workflowItemService;
    private Grid<Order> ordersGrid;
    private List<Order> orders = new ArrayList<>();
    private GridListDataView<Order> ordersView;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public OrderList(OrderService orderService, WorkflowNodeService workflowNodeService, WorkflowItemService workflowItemService) {
        this.orderService = orderService;
        this.workflowNodeService = workflowNodeService;
        this.workflowItemService = workflowItemService;
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
                List<WorkflowNode> nodes = workflowNodeService.getAll(selectedOrder.getWorkflowId());
                dialog.add(new WorkflowView(nodes, workflowNodeService.getById(selectedOrder.getCurrentNode())));
                dialog.open();
            });
            Button historyButton = new Button("Show history");
            historyButton.addClickListener(e -> {
                WorkflowItemHistoryDialog dialog = new WorkflowItemHistoryDialog(workflowItemService.getWorkflowItemHistory(selectedOrder));
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
