package de.ketobi.vaadinspringdemo.apps.orders;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes.ORDER_WORKFLOW;

@Route(value = "createOrder", layout = MainLayout.class)
@PageTitle("Create order")
public class CreateOrder extends VerticalLayout implements BeforeEnterObserver {
    private final OrderService orderService;
    private final WorkflowService workflowService;
    private final WorkflowItemService workflowItemService;
    private TextField item = new TextField("Item to order *");
    private TextArea description = new TextArea("Description");
    private TextArea reason = new TextArea("Reason");

    private TextField supplier = new TextField("Supplier");
    private NumberField price = new NumberField("Price");

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }

    public CreateOrder(OrderService orderService, WorkflowService workflowService, WorkflowItemService workflowItemService){

        this.orderService = orderService;
        this.workflowService = workflowService;
        this.workflowItemService = workflowItemService;
        add(item);
        add(description);
        add(reason);
        add(supplier);
        price.setValue(0.0);
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        price.setSuffixComponent(euroSuffix);
        add(price);
        add(new Button("Save", e -> {
            Order order = Order.builder()
                    .id(ObjectId.get())
                    .item(item.getValue())
                    .description(description.getValue())
                    .reason(reason.getValue())
                    .supplier(supplier.getValue())
                    .price(BigDecimal.valueOf(price.getValue()))
                    .createdBy(UserService.getCurrentUser().getId())
                    .createdAt(LocalDateTime.now())
                    .build();
            order.setWorkflow(workflowService.getByName(ORDER_WORKFLOW.getName()));
            order.setCurrentNode(workflowService.getStartNode(order.getWorkflowId()));
            orderService.save(order);
            workflowItemService.startWorkflow(order);
            item.clear();
            description.clear();
            reason.clear();
            supplier.clear();
            price.clear();
        }));
    }

}
