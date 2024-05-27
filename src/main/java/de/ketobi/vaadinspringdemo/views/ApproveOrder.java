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

@Route(value = "665461bacb82ed217dceb578", layout = MainLayout.class)
@PageTitle("Approve Order")
public class ApproveOrder extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final OrderRepository orderRepository;
    private Order order;
    private TextField itemField;
    private TextField descriptionField;
    private TextField supplierField;
    private TextField priceField;

    public ApproveOrder(OrderRepository orderRepository, WorkflowItemService workflowItemService){
        this.orderRepository = orderRepository;
        add(new H3("Approve Order"));
        add(new Paragraph("Please review the order details."));
        itemField = new TextField("Item");
        itemField.setReadOnly(true);

        descriptionField = new TextField("Description");
        descriptionField.setReadOnly(true);

        supplierField = new TextField("Supplier");
        supplierField.setReadOnly(true);

        priceField = new TextField("Price");
        priceField.setReadOnly(true);

        Button approveButton = new Button("Approve");
        approveButton.addClickListener(e -> {
            workflowItemService.nextNode(order, true, "The order has been approved.");
            approveButton.getUI().ifPresent(ui -> ui.navigate("todos"));
        });

        Button declineButton = new Button("Decline");
        declineButton.addClickListener(e -> {
            workflowItemService.nextNode(order, false, "The order has been declined.");
            declineButton.getUI().ifPresent(ui -> ui.navigate("todos"));
        });

        add(itemField, descriptionField, supplierField, priceField);
        add(approveButton, declineButton);
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