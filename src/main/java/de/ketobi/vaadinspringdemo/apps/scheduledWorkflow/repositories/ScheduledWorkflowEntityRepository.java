package de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.repositories;

import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.entities.ScheduledWorkflowEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledWorkflowEntityRepository extends MongoRepository<ScheduledWorkflowEntity, ObjectId> {
}
