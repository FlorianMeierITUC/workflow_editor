package de.ketobi.vaadinspringdemo.apps.orders.batchnodes;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("OrderSendMail")
public class OrderSendMail implements Batchnode {
    private final OrderService orderService;
    private final WorkflowItemService workflowItemService;
    private final WorkflowTicketService workflowTicketService;

    public OrderSendMail(OrderService orderService, WorkflowItemService workflowItemService, WorkflowTicketService workflowTicketService){
        this.orderService = orderService;
        this.workflowItemService = workflowItemService;
        this.workflowTicketService = workflowTicketService;
    }
    @Override
    public void execute(ObjectId ticketId) {
        //This is a dummy implementation
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);
        Order order = orderService.getOrderById(workflowTicket.getWorkflowEntityId());
        workflowItemService.nextNode(workflowTicket, null, "Mail sent");
    }
}
