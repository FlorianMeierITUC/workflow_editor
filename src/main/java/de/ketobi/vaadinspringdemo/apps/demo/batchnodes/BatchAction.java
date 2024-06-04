package de.ketobi.vaadinspringdemo.apps.demo.batchnodes;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("664e4e839228b34647a7839e")
public class BatchAction extends Batchnode {

    private final DemoObjectService demoObjectService;

    public BatchAction(DemoObjectService demoObjectService){
        this.demoObjectService = demoObjectService;
    }

    @Override
    public void execute(ObjectId ticketId) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);

        //Do something with the entity or not. Whatever floats your boat.
        DemoObject demoObject = demoObjectService.getById(workflowTicket.getEntityId());
        demoObject.setResultBatchAction("Batch Action executed");
        demoObjectService.save(demoObject);

        workflowEntityService.nextNode(workflowTicket, null, "Batch Action executed");
    }
}
