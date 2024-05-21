package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.WorkflowItemHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowItemHistoryRepository extends MongoRepository<WorkflowItemHistory, ObjectId> {
}
