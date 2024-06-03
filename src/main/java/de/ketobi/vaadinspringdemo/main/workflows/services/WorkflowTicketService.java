package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowTicketRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkflowTicketService {
    private final MongoTemplate mongoTemplate;
    private final WorkflowTicketRepository workflowTicketRepository;

    public WorkflowTicketService(MongoTemplate mongoTemplate, WorkflowTicketRepository workflowTicketRepository) {
        this.mongoTemplate = mongoTemplate;
        this.workflowTicketRepository = workflowTicketRepository;
    }

    public WorkflowEntity getWorkflowEntity(WorkflowTicket workflowTicket) {
        return mongoTemplate.findById(workflowTicket.getWorkflowEntityId(), WorkflowTypes.fromId(workflowTicket.getWorkflowId()).getEntity());
    }

    public WorkflowTicket getWorkflowTicket(ObjectId workflowTicketId) {
        return workflowTicketRepository.findById(workflowTicketId).orElseThrow();
    }

    public void save(WorkflowTicket selectedWfTicket) {
        workflowTicketRepository.save(selectedWfTicket);
    }
}
