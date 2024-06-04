package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
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
    private final WorkflowNodeService workflowNodeService;
    private final UserService userService;

    public WorkflowTicketService(MongoTemplate mongoTemplate, WorkflowTicketRepository workflowTicketRepository, WorkflowNodeService workflowNodeService, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.workflowTicketRepository = workflowTicketRepository;
        this.workflowNodeService = workflowNodeService;
        this.userService = userService;
    }

    public WorkflowEntity getWorkflowEntity(WorkflowTicket workflowTicket) {
        return mongoTemplate.findById(workflowTicket.getEntityId(), WorkflowTypes.fromId(workflowTicket.getWorkflowId()).getEntity());
    }

    public WorkflowTicket getWorkflowTicket(ObjectId workflowTicketId) {
        return workflowTicketRepository.findById(workflowTicketId).orElseThrow();
    }

    public void save(WorkflowTicket selectedWfTicket) {
        workflowTicketRepository.save(selectedWfTicket);
    }

    public WorkflowNode getCurrentNode(ObjectId workflowEntityId) {
        WorkflowTicket ticket = workflowTicketRepository.findByEntityId(workflowEntityId).orElseThrow();
        return workflowNodeService.getById(ticket.getCurrentNodeId());
    }

    public User getCurrentResponsible(ObjectId workflowEntityId) {
        WorkflowTicket ticket = workflowTicketRepository.findByEntityId(workflowEntityId).orElseThrow();
        return userService.getUserById(ticket.getCurrentResponsibleId());
    }
}
