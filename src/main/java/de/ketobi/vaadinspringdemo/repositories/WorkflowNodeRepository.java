package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowNodeRepository extends MongoRepository<WorkflowNode, String> {
}
