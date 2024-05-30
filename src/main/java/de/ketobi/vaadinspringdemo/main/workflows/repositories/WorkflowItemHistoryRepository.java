package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItemHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface WorkflowItemHistoryRepository extends MongoRepository<WorkflowItemHistory, ObjectId> {
    ArrayList<WorkflowItemHistory> findByItemId(ObjectId id);
}
