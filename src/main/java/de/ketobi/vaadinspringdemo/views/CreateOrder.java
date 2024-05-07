package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.Order;
import de.ketobi.vaadinspringdemo.repositories.OrderRepository;

import java.math.BigDecimal;

@Route(value = "createOrder", layout = MainLayout.class)
@PageTitle("Create order")
public class CreateOrder extends VerticalLayout {
    private final OrderRepository orderRepository;
    private TextField item = new TextField("Item to order *");
    private TextArea description = new TextArea("Description");
    private TextArea reason = new TextArea("Reason");

    private TextField supplier = new TextField("Supplier");
    private TextField price = new TextField("Price in Euro");

    public CreateOrder(OrderRepository orderRepository){

        this.orderRepository = orderRepository;
        add(item);
        add(description);
        add(reason);
        add(supplier);
        add(price);
        add(new Button("Save", e -> {
            Order order = Order.builder()
                    .item(item.getValue())
                    .description(description.getValue())
                    .reason(reason.getValue())
                    .supplier(supplier.getValue())
                    .price(price.getValue().isEmpty() ? null : new BigDecimal(price.getValue()))
                    .build();
            orderRepository.save(order);
            item.clear();
            description.clear();
            reason.clear();
            supplier.clear();
            price.clear();
        }));
    }

}
