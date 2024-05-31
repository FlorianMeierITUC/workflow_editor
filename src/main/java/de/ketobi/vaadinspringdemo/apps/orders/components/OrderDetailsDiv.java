package de.ketobi.vaadinspringdemo.apps.orders.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;

import java.time.format.DateTimeFormatter;

public class OrderDetailsDiv extends Div {
    private final OrderService orderService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final Order order;

    public OrderDetailsDiv(Order order, OrderService orderService) {
        this.order = order;
        this.orderService = orderService;

        setId("order-details-div");
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H4("Order details for order " + order.getTitle()));
        layout.add(new Paragraph("ID: " + order.getId()));
        layout.add(new Paragraph("Item: " + order.getTitle()));
        layout.add(new Paragraph("Description: " + order.getDescription()));
        layout.add(new Paragraph("Reason: " + order.getReason()));
        layout.add(new Paragraph("Price: " + order.getPrice()));
        layout.add(new Paragraph("Created by: " + orderService.getCreatedBy(order).getName()));
        layout.add(new Paragraph("Created at: " + order.getCreatedAt().format(formatter)));
        layout.add(new Paragraph("Order current node: " + orderService.getCurrentNode(order).getTitle()));
        layout.add(new Paragraph("Order current responsible: " + orderService.getCurrentResponsible(order).getName()));
        add(layout);
    }

}
