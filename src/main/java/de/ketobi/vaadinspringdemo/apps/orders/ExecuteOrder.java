package de.ketobi.vaadinspringdemo.apps.orders;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;

@Route(value = "664dd9de49a7d57f42c0a1e7", layout = MainLayout.class)
@PageTitle("Execute order")
public class ExecuteOrder extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final OrderService orderService;
    private final WorkflowTicketService workflowTicketService;
    private WorkflowTicket workflowTicket;
    private Order order;
    private TextField itemField;
    private TextField descriptionField;
    private TextField supplierField;
    private TextField priceField;
    private TextField message;
    private TextField orderNumber;

    public ExecuteOrder(OrderService orderService, WorkflowItemService workflowItemService, WorkflowTicketService workflowTicketService){
        this.orderService = orderService;
        this.workflowTicketService = workflowTicketService;
        add(new H3("Execute order"));
        add(new H4("Please contact the supplier and place the order. After you have received the order number, please enter it here."));
        itemField = new TextField("Item");
        itemField.setReadOnly(true);

        descriptionField = new TextField("Description");
        descriptionField.setReadOnly(true);

        supplierField = new TextField("Supplier");
        supplierField.setReadOnly(true);

        priceField = new TextField("Price");
        priceField.setReadOnly(true);

        message = new TextField("Message");
        orderNumber = new TextField("Order number");

        Button executedButton = new Button("Order executed");
        executedButton.addClickListener(e -> {
            order.setOrderNumber(orderNumber.getValue());
            orderService.save(order);
            workflowItemService.nextNode(workflowTicket, null,  message.getValue());
            executedButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            cancelButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        add(itemField, descriptionField, supplierField, priceField);
        add(new Paragraph("Please add a message. This message will be visible to the next user in the workflow."));
        add(message);
        add(new Paragraph("Please enter the order number provided by the supplier here."));
        add(orderNumber);
        add(executedButton, cancelButton);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String ticketId) {
        this.workflowTicket = workflowTicketService.getWorkflowTicket(new ObjectId(ticketId));
        this.order = orderService.getOrderById(workflowTicket.getWorkflowEntityId());
        itemField.setValue(order.getItem());
        descriptionField.setValue(order.getDescription());
        supplierField.setValue(order.getSupplier());
        priceField.setValue(order.getPrice().toString());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
}
