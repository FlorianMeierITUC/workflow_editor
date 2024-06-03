package de.ketobi.vaadinspringdemo.apps.demo.batchnodes;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("664e4e839228b34647a7839e")
public class BatchAction implements Batchnode {

    private final WorkflowItemService workflowItemService;
    private final WorkflowTicketService workflowTicketService;
    private final DemoObjectService demoObjectService;

    public BatchAction(WorkflowItemService workflowItemService, WorkflowTicketService workflowTicketService, DemoObjectService demoObjectService){
        this.workflowItemService = workflowItemService;
        this.workflowTicketService = workflowTicketService;
        this.demoObjectService = demoObjectService;
    }

    @Override
    public void execute(ObjectId workflowTicketID) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(workflowTicketID);

        //Do something with the entity or not. Whatever floats your boat.
        DemoObject demoObject = demoObjectService.getById(workflowTicket.getWorkflowEntityId());
        demoObject.setResultBatchAction("Batch Action executed");
        demoObjectService.save(demoObject);

        workflowItemService.nextNode(workflowTicket, null, "Batch Action executed");
    }
}
