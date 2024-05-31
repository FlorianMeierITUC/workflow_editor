package de.ketobi.vaadinspringdemo.apps.orders.services;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WorkflowNodeService workflowNodeService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, WorkflowNodeService workflowNodeService, UserService userService) {
        this.orderRepository = orderRepository;
        this.workflowNodeService = workflowNodeService;
        this.userService = userService;
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

    public ArrayList<Order> getAllOrdersCreatedByTheCurrentUser() {
        return new ArrayList<>(orderRepository.findByCreatedBy(UserService.getCurrentUser().getId()));
    }

    public WorkflowNode getCurrentNode(Order order) {
        return workflowNodeService.getById(order.getCurrentNode());
    }

    public User getCurrentResponsible(Order order) {
        return userService.getUserById(order.getCurrentResponsible());
    }
}