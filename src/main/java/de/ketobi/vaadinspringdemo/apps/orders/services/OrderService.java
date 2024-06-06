package de.ketobi.vaadinspringdemo.apps.orders.services;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WorkflowTicketService workflowTicketService;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, WorkflowTicketService workflowTicketService) {
        this.orderRepository = orderRepository;
        this.workflowTicketService = workflowTicketService;
        this.userService = userService;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
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
        if (workflowTicketService.getCurrentNodes(order.getId()).size()!=1 ){
            throw new RuntimeException("There should be exactly one current node for order " + order.getId());
        }
        return workflowTicketService.getCurrentNodes(order.getId()).get(0);
    }

    public User getCurrentResponsible(Order order) {
        if (workflowTicketService.getWorkflowTickets(order.getId()).size()!=1 ){
            throw new RuntimeException("There should be exactly one ticket for order " + order.getId());
        }
        WorkflowTicket ticket = workflowTicketService.getWorkflowTickets(order.getId()).get(0);
        return workflowTicketService.getCurrentResponsible(ticket.getId());
    }
}