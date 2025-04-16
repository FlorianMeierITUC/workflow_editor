package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowTicketRepository extends MongoRepository<WorkflowTicket, ObjectId> {
    List<WorkflowTicket> findByCurrentResponsibleId(ObjectId idUser);
    List<WorkflowTicket> findByEntityId(ObjectId workflowEntityId);
}
