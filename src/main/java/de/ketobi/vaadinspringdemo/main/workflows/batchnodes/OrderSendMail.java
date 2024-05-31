package de.ketobi.vaadinspringdemo.main.workflows.batchnodes;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("OrderSendMail")
public class OrderSendMail implements Batchnode{
    WorkflowItemService workflowItemService;
    OrderRepository orderRepository;

    public OrderSendMail(WorkflowItemService workflowItemService, OrderRepository orderRepository){
        this.workflowItemService = workflowItemService;
        this.orderRepository = orderRepository;
    }
    @Override
    public void execute(ObjectId itemId) {
        //This is a dummy implementation
        Order order = orderRepository.findById(itemId).orElseThrow();
        workflowItemService.nextNode(order, null, "Mail sent");
    }
}
