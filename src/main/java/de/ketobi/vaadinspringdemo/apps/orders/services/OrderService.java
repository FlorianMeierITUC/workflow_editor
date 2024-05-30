package de.ketobi.vaadinspringdemo.apps.orders.services;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.repositories.OrderRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(new ObjectId(id)).orElseThrow();
    }

    public Order getOrderById(ObjectId id) {
        return orderRepository.findById(id).orElseThrow();
    }

}