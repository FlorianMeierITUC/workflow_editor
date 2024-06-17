package de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.batchnodes;

import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.entities.ScheduledWorkflowEntity;
import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.services.ScheduledObjectService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("666a9926e482a81681204765")
public class NotifyScheduledObject extends Batchnode {
    private final ScheduledObjectService scheduledObjectService;

    public NotifyScheduledObject(ScheduledObjectService scheduledObjectService){
        this.scheduledObjectService = scheduledObjectService;
    }

    @Override
    public void execute(ObjectId ticketId) {
        WorkflowTicket workflowTicket = workflowTicketService.getWorkflowTicket(ticketId);

        ScheduledWorkflowEntity scheduledObject = scheduledObjectService.getById(workflowTicket.getEntityId());
        scheduledObject.setResultScheduledBatchAction("Scheduled batch action executed");
        scheduledObjectService.save(scheduledObject);

        workflowEntityService.nextNode(workflowTicket, null, "Scheduled batch action executed");
    }
}
