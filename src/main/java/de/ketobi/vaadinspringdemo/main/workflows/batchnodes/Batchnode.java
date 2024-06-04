package de.ketobi.vaadinspringdemo.main.workflows.batchnodes;

import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Batchnode {
    @Autowired
    protected WorkflowEntityService workflowEntityService;
    @Autowired
    protected WorkflowTicketService workflowTicketService;

    public abstract void execute(ObjectId ticketId);
}
