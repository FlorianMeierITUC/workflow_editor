package de.ketobi.vaadinspringdemo.batchnodes;

import de.ketobi.vaadinspringdemo.entities.Order;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.repositories.OrderRepository;
import de.ketobi.vaadinspringdemo.services.WorkflowItemService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("OrderPriceCheck")
public class OrderPriceCheck implements Batchnode {
    private final OrderRepository orderRepository;
    private final WorkflowItemService workflowItemService;

    public OrderPriceCheck(OrderRepository orderRepository, WorkflowItemService workflowItemService){
        this.orderRepository = orderRepository;
        this.workflowItemService = workflowItemService;
    }

    @Override
    public void execute(ObjectId itemId) {
        Order order = orderRepository.findById(itemId).orElseThrow();

        if(order.getPrice().doubleValue()<100.0){
            workflowItemService.nextNode(order, true,  "Order price is < 100€");
        }else{
            workflowItemService.nextNode(order, false,  "Order price is >= 100€");
        }
    }
}
