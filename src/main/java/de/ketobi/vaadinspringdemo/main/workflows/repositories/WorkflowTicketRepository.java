package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkflowTicketRepository extends MongoRepository<WorkflowTicket, ObjectId> {
    List<WorkflowTicket> findByCurrentResponsibleId(ObjectId idUser);
    List<WorkflowTicket> findByEntityId(ObjectId workflowEntityId);
}
