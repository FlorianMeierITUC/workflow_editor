package de.ketobi.vaadinspringdemo.entities;

import de.ketobi.vaadinspringdemo.services.WorkflowItemService;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class WorkflowItem {
    private Workflow  workflow;
    private WorkflowNode currentNode;
}
