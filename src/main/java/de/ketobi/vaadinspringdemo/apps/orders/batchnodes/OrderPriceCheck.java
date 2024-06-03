package de.ketobi.vaadinspringdemo.apps.orders.batchnodes;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("665461f4cb82ed217dceb579")
public class OrderPriceCheck implements Batchnode {
    private final OrderService orderService;
    private final WorkflowItemService workflowItemService;
    private final WorkflowTicketService workflowTicketService;

    public OrderPriceCheck(OrderService orderService, WorkflowItemService workflowItemService, WorkflowTicketService workflowTicketService){
        this.orderService = orderService;
        this.workflowItemService = workflowItemService;
        this.workflowTicketService = workflowTicketService;
    }

    @Override
    public void execute(ObjectId workflowTicketID) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(workflowTicketID);
        Order order = orderService.getOrderById(workflowTicket.getWorkflowEntityId());

        if(order.getPrice().doubleValue()<100.0){
            workflowItemService.nextNode(workflowTicket, true,  "Order price is < 100€");
        }else{
            workflowItemService.nextNode(workflowTicket, false,  "Order price is >= 100€");
        }
    }
}
