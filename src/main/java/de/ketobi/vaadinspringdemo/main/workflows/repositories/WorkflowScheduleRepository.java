package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowScheduleRepository extends MongoRepository<WorkflowSchedule, ObjectId> {
    boolean existsByWorkflowId(ObjectId workflowId);
    WorkflowSchedule findByWorkflowId(ObjectId workflowId);
    void deleteByWorkflowId(ObjectId workflowId);
}
