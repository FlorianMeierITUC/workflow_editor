package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.*;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowTicketHistoryRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowTicketRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkflowEntityService {
    private final WorkflowService workflowService;
    private final WorkflowTicketHistoryRepository historyRepository;
    private final WorkflowNodeRepository nodeRepository;
    private final WorkflowTicketService workflowTicketService;
    private final WorkflowTicketRepository workflowTicketRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final ApplicationContext context;
    private final WorkflowNodeService workflowNodeService;


    @Autowired
    public WorkflowEntityService(
            WorkflowTicketHistoryRepository historyRepository,
            WorkflowNodeRepository nodeRepository,
            WorkflowTicketRepository workflowTicketRepository,
            WorkflowTicketService workflowTicketService,
            UserService userService,
            WorkflowService workflowService,
            MongoTemplate mongoTemplate,
            ApplicationContext context, WorkflowNodeService workflowNodeService) {
        this.workflowService = workflowService;
        this.historyRepository = historyRepository;
        this.nodeRepository = nodeRepository;
        this.workflowTicketRepository = workflowTicketRepository;
        this.workflowTicketService = workflowTicketService;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
        this.context = context;
        this.workflowNodeService = workflowNodeService;
    }

    public ArrayList<WorkflowTicketHistory> getWorkflowEntityHistory(WorkflowEntity workflowEntity) {
        return historyRepository.findByEntityId(workflowEntity.getId());
    }

    public String getWorkflowEntityStatus(WorkflowEntity workflowEntity) {
        List<WorkflowTicket> tickets = workflowTicketRepository.findByEntityId(workflowEntity.getId());
        if (tickets.size() == 0) {
            return "Not started";
        }
        WorkflowTicket lastTicket = tickets.get(tickets.size() - 1);
        WorkflowNode lastNode = nodeRepository.findById(lastTicket.getCurrentNodeId()).orElseThrow();
        if (lastNode.getType() == WorkflowNodeTypes.END) {
            return "Finished";
        }
        return "In progress";
    }

    public List<WorkflowTicket> getAllWorkflowTicketsAssignedToTheCurrentUser() {
        return workflowTicketRepository.findByCurrentResponsibleId(UserService.getCurrentUser().getId());
    }

    public List<WorkflowEntity> getAllWorkflowEntitiesCreatedByTheCurrentUser() {
        List<WorkflowEntity> workflowEntities = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            List<? extends WorkflowEntity> entities = mongoTemplate.findAll(type.getEntity());
            for(Object entity : entities){
                WorkflowEntity workflowEntity = (WorkflowEntity) entity;
                if(workflowEntity.getCreatedBy()!=null && workflowEntity.getCreatedBy().equals(UserService.getCurrentUser().getId())){
                    workflowEntities.add(workflowEntity);
                }
            }
        }
        return workflowEntities;
    }
    public WorkflowEntity getWorkflowEntity(ObjectId workflowEntityId, ObjectId workflowId) {
        return mongoTemplate.findById(workflowEntityId, WorkflowTypes.fromId(workflowId).getEntity());
    }

    public void startWorkflow(WorkflowEntity workflowEntity) {
        WorkflowNode startNode = workflowService.getStartNode(workflowEntity.getWorkflowType().getId());
        WorkflowTicket ticket = WorkflowTicket.builder()
                .id(ObjectId.get())
                .workflowId(workflowEntity.getWorkflowType().getId())
                .currentResponsibleId(startNode.getResponsible())
                .currentNodeId(startNode.getId())
                .entityId(workflowEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
        workflowTicketRepository.save(ticket);
        nextNode(ticket, null, "Workflow started");
    }

    public void nextNode(WorkflowTicket workflowTicket, Boolean success, String message) {
        WorkflowNode currentNode = nodeRepository.findById(workflowTicket.getCurrentNodeId()).orElseThrow();
        if(currentNode == null){
            throw new RuntimeException("Current node is null! That should not be possible... Database corrupted?");
        }
        Workflow wf = workflowService.getById(workflowTicket.getWorkflowId());
        System.out.println("Workflow: "+wf.getName()+" Current node: " + currentNode.getTitle());
        User responsible;
        if(currentNode.getResponsible()==null) {
            responsible = UserService.getSystemUser();
        } else if (currentNode.getResponsible().equals(UserService.getSystemUser().getId())) {
            responsible = UserService.getSystemUser();
        } else {
            responsible = userService.getUserById(workflowTicket.getCurrentResponsibleId());
        }
        System.out.println("Responsible: "+responsible.getName());

        WorkflowEntity workflowEntity = workflowTicketService.getWorkflowEntity(workflowTicket);
        System.out.println("Entity: "+workflowEntity.getName());

        WorkflowTicketHistory history = WorkflowTicketHistory.builder()
                .ticketId(workflowTicket.getId())
                .workflowName(wf.getName())
                .nodeTitle(currentNode.getTitle())
                .entityId(workflowEntity.getId())
                .message(message)
                .responsibleUser(responsible.getName())
                .createdAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
        ObjectId nextNodeId = null;
        switch (currentNode.getType()) {
            case BATCH_DECISION:
            case USER_DECISION:
                System.out.println("Decision node");
                if (success) {
                    nextNodeId = currentNode.getSuccessorNode_success();
                } else {
                    nextNodeId = currentNode.getSuccessorNode_failure();
                }
                workflowTicket.setCurrentNodeId(nextNodeId);
                processNextNode(nextNodeId, workflowTicket, wf, workflowEntity);
                break;
            case BATCH_ACTION:
            case START:
            case USER_ACTION:
                System.out.println("Start or Action node");
                nextNodeId = currentNode.getSuccessorNodes().get(0);
                workflowTicket.setCurrentNodeId(nextNodeId);
                processNextNode(nextNodeId, workflowTicket, wf, workflowEntity);
                break;
            case UNION:
                System.out.println("Union node");
                //Check if all siblings are in this union node if so go to the next node otherwise stay in this node
                boolean canAdvance = true;
                List<ObjectId> siblings = workflowTicket.getSiblingIds();
                if (siblings != null && siblings.size() > 0) {
                    for (ObjectId siblingId : siblings) {
                        WorkflowTicket sibling = workflowTicketRepository.findById(siblingId).orElseThrow();
                        if (!sibling.getCurrentNodeId().equals(workflowTicket.getCurrentNodeId())) {
                            canAdvance = false;
                            break;
                        }
                    }
                } else {
                    throw new RuntimeException("Siblings are null or < 1! That should not be possible... Database corrupted? Workflow invalid?");
                }
                if (canAdvance) {
                    nextNodeId = currentNode.getSuccessorNodes().get(0);
                    workflowTicket.setCurrentNodeId(nextNodeId);
                    WorkflowTicketHistory historyUnion = WorkflowTicketHistory.builder()
                            .ticketId(workflowTicket.getId())
                            .workflowName(wf.getName())
                            .nodeTitle(currentNode.getTitle())
                            .entityId(workflowEntity.getId())
                            .message("Union node passed")
                            .responsibleUser(responsible.getName())
                            .createdAt(LocalDateTime.now())
                            .build();
                    historyRepository.save(historyUnion);
                    processNextNode(nextNodeId, workflowTicket, wf, workflowEntity);
                    cleanUpTickets(workflowTicket, siblings);
                }
                break;
            case END:
                System.out.println("End node");
                //Do nothing
                break;
            case AND:
                System.out.println("And node");
                //create the siblings and advance them to the next node
                ArrayList<WorkflowNode> successorNodes = currentNode.getSuccessorNodes().stream()
                        .map(this::getWorkflowNodeById)
                        .collect(Collectors.toCollection(ArrayList::new));
                Map<ObjectId, WorkflowTicket> siblingMap = new HashMap<>();
                Map<WorkflowNode, WorkflowTicket> successorNodeToTicketMap = new HashMap<>();
                for (WorkflowNode successorNode : successorNodes) {
                    System.out.println("Creating sibling for node: "+successorNode.getTitle());
                    ObjectId ticketId = ObjectId.get();
                    ObjectId responsibleId = successorNode.getResponsible();
                    if(successorNode.getResponsible()==null){
                        responsibleId = UserService.getSystemUser().getId();
                    }
                    WorkflowTicket sibling = WorkflowTicket.builder()
                            .id(ticketId)
                            .workflowId(workflowTicket.getWorkflowId())
                            .currentResponsibleId(responsibleId)
                            .currentNodeId(successorNode.getId())
                            .entityId(workflowTicket.getEntityId())
                            .createdAt(LocalDateTime.now())
                            .siblingIds(workflowTicket.getSiblingIds())
                            .build();
                    siblingMap.put(ticketId, sibling);
                    successorNodeToTicketMap.put(successorNode, sibling);
                }
                for (WorkflowTicket sibling : siblingMap.values()) {
                    sibling.setSiblingIds(new ArrayList<>(siblingMap.keySet()));
                }
                workflowTicketRepository.saveAll(siblingMap.values());
                for(WorkflowNode successorNode : successorNodes){
                    if (successorNode.getType() == WorkflowNodeTypes.BATCH_DECISION || successorNode.getType() == WorkflowNodeTypes.BATCH_ACTION) {
                        Batchnode batchnode = (Batchnode) context.getBean(successorNode.getClassName());
                        try {
                            batchnode.execute(successorNodeToTicketMap.get(successorNode).getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //Delete the ticket still stuck in the AND node
                workflowTicketRepository.deleteById(workflowTicket.getId());
                break;
            default:
                throw new RuntimeException("Unknown node type");
        }
    }

    private void cleanUpTickets(WorkflowTicket workflowTicket, List<ObjectId> siblings) {
        //Delete siblings still stuck in the union node
        for (ObjectId siblingId : siblings) {
            if (!siblingId.equals(workflowTicket.getId())) {
                workflowTicketRepository.deleteById(siblingId);
            }
        }
    }

    private WorkflowNode getWorkflowNodeById(ObjectId nodeId) {
        return nodeRepository.findById(nodeId).orElseThrow();
    }

    public void writeWorkflowHistoryEntry(WorkflowTicketHistory history) {
        historyRepository.save(history);
    }

    private void processNextNode(ObjectId nextNodeId, WorkflowTicket workflowTicket, Workflow wf, WorkflowEntity workflowEntity){
        if (nextNodeId == null) {
            throw new RuntimeException("No next node found. Maybe nextNode() was called on an end node.");
        }
        WorkflowNode nextNode = getWorkflowNodeById(nextNodeId);
        if (nextNode.getType() == WorkflowNodeTypes.END) {
            WorkflowTicketHistory historyEnd = WorkflowTicketHistory.builder()
                    .id(workflowTicket.getId())
                    .workflowName(wf.getName())
                    .nodeTitle(nextNode.getTitle())
                    .entityId(workflowEntity.getId())
                    .message("Workflow finished")
                    .responsibleUser(UserService.getSystemUser().getName())
                    .createdAt(LocalDateTime.now())
                    .build();
            historyRepository.save(historyEnd);
        }
        if(nextNode.getResponsible()==null){
            workflowTicket.setCurrentResponsibleId(UserService.getSystemUser().getId());
        } else {
            workflowTicket.setCurrentResponsibleId(nextNode.getResponsible());
        }
        workflowTicketRepository.save(workflowTicket);

        if (nextNode.getType() == WorkflowNodeTypes.BATCH_DECISION || nextNode.getType() == WorkflowNodeTypes.BATCH_ACTION) {
            Batchnode batchnode = (Batchnode) context.getBean(nextNode.getClassName());
            try {
                batchnode.execute(workflowTicket.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (nextNode.getType() == WorkflowNodeTypes.AND) {
            nextNode(workflowTicket, null, "And node entered");
        }
        if (nextNode.getType() == WorkflowNodeTypes.UNION) {
            nextNode(workflowTicket, null, "Union node entered");
        }
    }
}
