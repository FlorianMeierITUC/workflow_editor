package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.entities.WorkflowNodeTypes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface WorkflowNodeRepository extends MongoRepository<WorkflowNode, String> {
    ArrayList<WorkflowNode> findByIdWorkflow(ObjectId idWorkflow);
    WorkflowNode findById(ObjectId id);
    WorkflowNode findByIdWorkflowAndType(ObjectId idWorkflow, WorkflowNodeTypes type);
}
