package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicketHistory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface WorkflowTicketHistoryRepository extends MongoRepository<WorkflowTicketHistory, ObjectId> {
    ArrayList<WorkflowTicketHistory> findByTicketId(ObjectId ticketId);
    ArrayList<WorkflowTicketHistory> findByEntityId(ObjectId entityId);
}
