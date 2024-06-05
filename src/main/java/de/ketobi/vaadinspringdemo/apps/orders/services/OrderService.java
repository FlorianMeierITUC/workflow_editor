package de.ketobi.vaadinspringdemo.apps.orders.services;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowTicketService workflowTicketService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, WorkflowNodeService workflowNodeService, UserService userService, WorkflowTicketService workflowTicketService) {
        this.orderRepository = orderRepository;
        this.workflowNodeService = workflowNodeService;
        this.workflowTicketService = workflowTicketService;
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

    public User getCreatedBy(Order order) {
        return userService.getUserById(order.getCreatedBy());
    }

    public WorkflowNode getCurrentNode(Order order) {
        return workflowTicketService.getCurrentNodes(order.getId()).get(0);
    }

    public User getCurrentResponsible(Order order) {
        //TODO this must be called with a ticket ID! Only tickets have responsibles!
        return workflowTicketService.getCurrentResponsible(order.getId());
    }
}