package de.ketobi.vaadinspringdemo.apps.demo.batchnodes;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component("Batch Action")
public class BatchAction implements Batchnode {

    private final WorkflowItemService workflowItemService;
    private final DemoObjectService demoObjectService;

    public BatchAction(WorkflowItemService workflowItemService, DemoObjectService demoObjectService){
        this.workflowItemService = workflowItemService;
        this.demoObjectService = demoObjectService;
    }

    @Override
    public void execute(ObjectId itemId) {
        DemoObject demoObject = demoObjectService.getById(itemId);
        workflowItemService.nextNode(demoObject, null, "Batch Action executed");
    }
}
