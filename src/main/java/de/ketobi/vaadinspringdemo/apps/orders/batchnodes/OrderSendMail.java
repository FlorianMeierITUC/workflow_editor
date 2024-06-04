package de.ketobi.vaadinspringdemo.apps.orders.batchnodes;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.orders.services.OrderService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("665462d1cb82ed217dceb57a")
public class OrderSendMail extends Batchnode {
    private final OrderService orderService;

    public OrderSendMail(OrderService orderService){
        this.orderService = orderService;
    }
    @Override
    public void execute(ObjectId ticketId) {
        //This is a dummy implementation
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);
        //Do something with the order. Or not...
        Order order = orderService.getOrderById(workflowTicket.getEntityId());
        workflowEntityService.nextNode(workflowTicket, null, "Mail sent");
    }
}
