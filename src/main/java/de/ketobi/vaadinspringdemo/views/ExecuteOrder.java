package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.entities.Order;
import de.ketobi.vaadinspringdemo.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.services.UserService;
import de.ketobi.vaadinspringdemo.services.WorkflowItemService;
import org.bson.types.ObjectId;

@Route(value = "664dd9de49a7d57f42c0a1e7", layout = MainLayout.class)
@PageTitle("Execute order")
public class ExecuteOrder extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final OrderRepository orderRepository;
    private final WorkflowItemService workflowItemService;
    private String orderId;
    private Order order;
    private TextField itemField;
    private TextField descriptionField;
    private TextField supplierField;
    private TextField priceField;
    private TextField message;
    private TextField orderNumber;

    public ExecuteOrder(OrderRepository orderRepository, WorkflowItemService workflowItemService){
        this.orderRepository = orderRepository;
        this.workflowItemService = workflowItemService;
        add(new H3("Execute order"));
        add(new Paragraph("Bitte führen sie die Bestellung durch."));
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
            orderRepository.save(order);
            workflowItemService.nextNode(workflowItemService.getWorkflowNodeById(new ObjectId("664dd9de49a7d57f42c0a1e7")), order, null,  message.getValue());
            executedButton.getUI().ifPresent(ui -> ui.navigate("todos"));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            cancelButton.getUI().ifPresent(ui -> ui.navigate("todos"));
        });

        add(itemField, descriptionField, supplierField, priceField);
        add(new Paragraph("Wenn sie möchten können sie hier ein Kommentar anhängen"));
        add(message);
        add(new Paragraph("Wenn sie nach der durchführung der Bestellung eine Bestellnummer erhalten haben geben sie diese bitte hier ein."));
        add(orderNumber);
        add(executedButton, cancelButton);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.order = orderRepository.findById(new ObjectId(s)).orElseThrow();
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
