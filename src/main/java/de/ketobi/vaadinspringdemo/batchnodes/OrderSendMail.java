package de.ketobi.vaadinspringdemo.batchnodes;

import de.ketobi.vaadinspringdemo.entities.Order;
import de.ketobi.vaadinspringdemo.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.services.WorkflowItemService;
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
        Order order = orderRepository.findById(itemId).orElseThrow();

        System.out.println("Sending mail for order with id: "+itemId);
        workflowItemService.nextNode(order, null, "Mail sent");
    }
}
