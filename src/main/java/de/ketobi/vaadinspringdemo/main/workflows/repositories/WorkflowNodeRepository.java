package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface WorkflowNodeRepository extends MongoRepository<WorkflowNode, String> {
    ArrayList<WorkflowNode> findByIdWorkflow(ObjectId idWorkflow);
    WorkflowNode findById(ObjectId id);
    WorkflowNode findByIdWorkflowAndType(ObjectId idWorkflow, WorkflowNodeTypes type);
}
