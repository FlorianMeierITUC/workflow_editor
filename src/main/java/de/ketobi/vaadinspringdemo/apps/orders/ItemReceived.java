package de.ketobi.vaadinspringdemo.apps.orders;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;

@Route(value = "66621bda5161bb2c9a93a1f0", layout = MainLayout.class)
@PageTitle("Item Received")
public class ItemReceived extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final OrderService orderService;
    private final WorkflowTicketService workflowTicketService;
    private WorkflowTicket workflowTicket;
    private Order order;
    private TextField itemField;
    private TextField descriptionField;
    private TextField supplierField;
    private TextField priceField;
    private TextField message;
    private Checkbox itemReceived;

    public ItemReceived(OrderService orderService, WorkflowEntityService workflowEntityService, WorkflowTicketService workflowTicketService){
        this.orderService = orderService;
        this.workflowTicketService = workflowTicketService;
        add(new H3("Item Received"));
        add(new H4("Please confirm that the item has been received."));
        itemField = new TextField("Item");
        itemField.setReadOnly(true);

        descriptionField = new TextField("Description");
        descriptionField.setReadOnly(true);

        supplierField = new TextField("Supplier");
        supplierField.setReadOnly(true);

        priceField = new TextField("Price");
        priceField.setReadOnly(true);

        message = new TextField("Message");
        itemReceived = new Checkbox("Item received");

        Button receivedButton = new Button("Confirm received");
        receivedButton.addClickListener(e -> {
            if (null == itemReceived.getValue() || !itemReceived.getValue()){
                Notification notification = Notification.show("You can only advance the workflow when the item has been received!.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            order.setItemReceived(itemReceived.getValue());
            orderService.save(order);
            workflowEntityService.nextNode(workflowTicket, null,  "Item received.");
            receivedButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            cancelButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });
        add(itemField, descriptionField, supplierField, priceField);
        add(new Paragraph("Please add a message. This message will be visible to the next user in the workflow."));
        add(message);
        add(new Paragraph("Please confirm that you received the ordered item."));
        add(itemReceived);
        add(receivedButton, cancelButton);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String ticketId) {
        this.workflowTicket = workflowTicketService.getWorkflowTicket(new ObjectId(ticketId));
        this.order = orderService.getOrderById(workflowTicket.getEntityId());
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