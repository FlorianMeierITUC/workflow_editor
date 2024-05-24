package de.ketobi.vaadinspringdemo.services;

import de.ketobi.vaadinspringdemo.entities.*;
import de.ketobi.vaadinspringdemo.repositories.WorkflowItemHistoryRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowItemService {
    private final WorkflowRepository workflowRepository;
    private final WorkflowItemHistoryRepository historyRepository;
    private final WorkflowNodeRepository nodeRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public WorkflowItemService(WorkflowItemHistoryRepository historyRepository, WorkflowNodeRepository nodeRepository, UserService userService, WorkflowRepository workflowRepository, MongoTemplate mongoTemplate) {
        this.workflowRepository = workflowRepository;
        this.historyRepository = historyRepository;
        this.nodeRepository = nodeRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    public ArrayList<WorkflowItemHistory> getWorkflowItemHistory(WorkflowItem workflowItem) {
        System.out.println("Getting workflow item history for "+workflowItem.getTitle()+" with id "+workflowItem.getId());
        return historyRepository.findByItemId(workflowItem.getId());
    }

    public WorkflowNode getWorkflowNodeById(ObjectId currentNodeId) {
        return nodeRepository.findById(currentNodeId);
    }


    public List<WorkflowItem> getAllWorkflowItemsAssignedToTheCurrentUser() {
        System.out.println("Getting all workflow items assigned to the current user");
        List<WorkflowItem> workflowItems = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            List<? extends WorkflowItem> items = mongoTemplate.findAll(type.getEntity());
            System.out.println("Number of Items: "+items.size());
            for(Object item : items){
                WorkflowItem workflowItem = (WorkflowItem) item;
                if(workflowItem.getCurrentResponsible()!=null && workflowItem.getCurrentResponsible().equals(UserService.getCurrentUser().getId())){
                    workflowItems.add(workflowItem);
                }
            }
        }
        return workflowItems;
    }

    public List<WorkflowItem> getAllWorkflowItemsCreatedByTheCurrentUser() {
        System.out.println("Getting all workflow items created by the current user");
        List<WorkflowItem> workflowItems = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            List<? extends WorkflowItem> items = mongoTemplate.findAll(type.getEntity());
            System.out.println("Number of Items: "+items.size());
            for(Object item : items){
                WorkflowItem workflowItem = (WorkflowItem) item;
                if(workflowItem.getCreatedBy()!=null && workflowItem.getCreatedBy().equals(UserService.getCurrentUser().getId())){
                    workflowItems.add(workflowItem);
                }
            }
        }
        return workflowItems;
    }

    public void startWorkflow(WorkflowItem workflowItem) {
        Workflow wf = workflowRepository.findById(workflowItem.getWorkflowId()).orElseThrow();
        workflowItem.setWorkflow(wf);
        WorkflowNode startNode = wf.getStartNode();
        workflowItem.setCurrentNode(startNode);
        workflowItem.setCurrentResponsible(startNode.getResponsible());
        System.out.println("Workflow started");
        System.out.println("Workflow item: " + workflowItem);
        System.out.println("Workflow: "+wf);
        System.out.println("Workflow start node: "+startNode);
        System.out.println("Current node: "+workflowItem.getCurrentNode());
        workflowItem.setMongoTemplate(mongoTemplate);
        workflowItem.save();
        nextNode(nodeRepository.findById(workflowItem.getCurrentNode()), workflowItem, null, "Workflow started");
    }

    public void nextNode(WorkflowNode currentNode, WorkflowItem workflowItem, Boolean success, String message) {
        System.out.println("Next node");
        Workflow wf = workflowRepository.findById(workflowItem.getWorkflowId()).orElseThrow();
        User responsible;
        if(currentNode.getResponsible()==null) {
            responsible = UserService.getSystemUser();
        } else if (currentNode.getResponsible().equals(UserService.getSystemUser().getId())) {
            responsible = UserService.getSystemUser();
        } else {
            responsible = userService.getUserById(workflowItem.getCurrentResponsible());
        }

        WorkflowItemHistory history = WorkflowItemHistory.builder()
                .itemId(workflowItem.getId())
                .workflowName(wf.getName())
                .nodeTitle(currentNode.getTitle())
                .itemTitle(workflowItem.getTitle())
                .message(message)
                .responsibleUser(responsible.getName())
                .createdAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
        System.out.println("Workflow history entry was created");
        WorkflowNode nextNode = null;
        System.out.println("Current node: "+currentNode);
        switch (currentNode.getType()) {
            case BATCH_DECISION:
            case USER_DECISION:
                if (success) {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_success());
                    System.out.println("Moving workflow item from "+currentNode+" to "+nextNode);
                    workflowItem.setCurrentNode(nextNode);
                } else {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_failure());
                    System.out.println("Moving workflow item from "+currentNode+" to "+nextNode);
                    workflowItem.setCurrentNode(nextNode);
                }
                break;
            case START:
            case USER_ACTION:
            case BATCH_ACTION:
            case UNION:
                nextNode = getWorkflowNodeById(currentNode.getSuccessorNodes().get(0));
                System.out.println("Moving workflow item from "+currentNode+" to "+nextNode);
                workflowItem.setCurrentNode(nextNode);
                break;
            case END:
                break;
            case OR:
            case AND:
                ArrayList<WorkflowNode> successorNodes = currentNode.getSuccessorNodes().stream()
                        .map(this::getWorkflowNodeById)
                        .collect(Collectors.toCollection(ArrayList::new));
                workflowItem.setCurrentNodes(successorNodes);
                break;
            default:
                throw new RuntimeException("Unknown node type");
        }

        if (nextNode == null) {
            throw new RuntimeException("No next node found. Maybe nextNode() was called on an end node.");
        }
        if(nextNode.getType() == WorkflowNodeTypes.END){
            WorkflowItemHistory historyEnd = WorkflowItemHistory.builder()
                    .itemId(workflowItem.getId())
                    .workflowName(wf.getName())
                    .nodeTitle(nextNode.getTitle())
                    .itemTitle(workflowItem.getTitle())
                    .message("Workflow finished")
                    .responsibleUser(UserService.getSystemUser().getName())
                    .createdAt(LocalDateTime.now())
                    .build();
            historyRepository.save(historyEnd);
        }
        System.out.println("Workflow item is now in the next node!");
        System.out.println("Next node: "+nextNode);
        workflowItem.setCurrentResponsible(nextNode.getResponsible());
        workflowItem.setMongoTemplate(mongoTemplate);
        workflowItem.save();
    }
}
