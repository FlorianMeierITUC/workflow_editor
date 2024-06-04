package de.ketobi.vaadinspringdemo.apps.orders.batchnodes;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("665461f4cb82ed217dceb579")
public class OrderPriceCheck extends Batchnode {
    private final OrderService orderService;

    public OrderPriceCheck(OrderService orderService){
        this.orderService = orderService;
    }

    @Override
    public void execute(ObjectId ticketId) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);
        Order order = orderService.getOrderById(workflowTicket.getEntityId());

        if(order.getPrice().doubleValue()<100.0){
            workflowEntityService.nextNode(workflowTicket, true,  "Order price is < 100€");
        }else{
            workflowEntityService.nextNode(workflowTicket, false,  "Order price is >= 100€");
        }
    }
}
