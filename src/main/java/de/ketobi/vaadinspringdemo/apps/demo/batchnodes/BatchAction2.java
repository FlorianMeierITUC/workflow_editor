package de.ketobi.vaadinspringdemo.apps.demo.batchnodes;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("66600ac63f842f6f8c92c738")
public class BatchAction2 extends Batchnode {
    private final DemoObjectService demoObjectService;

    public BatchAction2(DemoObjectService demoObjectService){
        this.demoObjectService = demoObjectService;
    }
    @Override
    public void execute(ObjectId ticketId) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);

        //Do something with the entity or not. Whatever floats your boat.
        DemoObject demoObject = demoObjectService.getById(workflowTicket.getEntityId());
        demoObject.setResultBatchAction2("Batch Action 2 executed");
        demoObjectService.save(demoObject);

        workflowEntityService.nextNode(workflowTicket, null, "Batch Action 2 executed");
    }
}
