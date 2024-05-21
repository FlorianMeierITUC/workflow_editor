package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.entities.WorkflowItemHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface WorkflowItemHistoryRepository extends MongoRepository<WorkflowItemHistory, ObjectId> {
    ArrayList<WorkflowItemHistory> findByItem(WorkflowItem item);
}
