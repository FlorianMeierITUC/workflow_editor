package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface WorkflowNodeRepository extends MongoRepository<WorkflowNode, ObjectId> {
    List<WorkflowNode> findByIdWorkflow(ObjectId idWorkflow);
    Optional<WorkflowNode> findByIdWorkflowAndType(ObjectId idWorkflow, WorkflowNodeTypes type);
}
