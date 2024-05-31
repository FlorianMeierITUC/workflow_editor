package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.batchnodes.Batchnode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.*;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowItemHistoryRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowItemService {
    private final WorkflowService workflowService;
    private final WorkflowItemHistoryRepository historyRepository;
    private final WorkflowNodeRepository nodeRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final ApplicationContext context;

    @Autowired
    public WorkflowItemService(
            WorkflowItemHistoryRepository historyRepository,
            WorkflowNodeRepository nodeRepository,
            UserService userService,
            WorkflowService workflowService,
            MongoTemplate mongoTemplate,
            ApplicationContext context) {
        this.workflowService = workflowService;
        this.historyRepository = historyRepository;
        this.nodeRepository = nodeRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
        this.context = context;
    }

    public ArrayList<WorkflowItemHistory> getWorkflowItemHistory(WorkflowItem workflowItem) {
        return historyRepository.findByItemId(workflowItem.getId());
    }

    public List<WorkflowItem> getAllWorkflowItemsAssignedToTheCurrentUser() {
        List<WorkflowItem> workflowItems = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            List<? extends WorkflowItem> items = mongoTemplate.findAll(type.getEntity());
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
        List<WorkflowItem> workflowItems = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            List<? extends WorkflowItem> items = mongoTemplate.findAll(type.getEntity());
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
        Workflow wf = workflowService.getById(workflowItem.getWorkflowId());
        workflowItem.setWorkflow(wf);
        WorkflowNode startNode = workflowService.getStartNode(wf.getId());
        workflowItem.setCurrentNode(startNode);
        workflowItem.setCurrentResponsible(startNode.getResponsible());
        workflowItem.setMongoTemplate(mongoTemplate);
        workflowItem.save();
        nextNode(workflowItem, null, "Workflow started");
    }

    public void nextNode(WorkflowItem workflowItem, Boolean success, String message) {
        WorkflowNode currentNode = nodeRepository.findById(workflowItem.getCurrentNode()).orElseThrow();
        if(currentNode == null){
            throw new RuntimeException("Current node is null! That should not be possible... Database corrupted?");
        }
        Workflow wf = workflowService.getById(workflowItem.getWorkflowId());
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
        WorkflowNode nextNode = null;
        switch (currentNode.getType()) {
            case BATCH_DECISION:
            case USER_DECISION:
                if (success) {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_success());
                } else {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_failure());
                }
                workflowItem.setCurrentNode(nextNode);
                break;
            case BATCH_ACTION:
            case START:
            case USER_ACTION:
            case UNION:
                nextNode = getWorkflowNodeById(currentNode.getSuccessorNodes().get(0));
                workflowItem.setCurrentNode(nextNode);
                break;
            case END:
                break;
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
        workflowItem.setCurrentResponsible(nextNode.getResponsible());
        workflowItem.setMongoTemplate(mongoTemplate);
        workflowItem.save();
        if(nextNode.getType() == WorkflowNodeTypes.BATCH_DECISION || nextNode.getType() == WorkflowNodeTypes.BATCH_ACTION){
            Batchnode batchnode = (Batchnode) context.getBean(nextNode.getExecutorClass());
            try {
                batchnode.execute(workflowItem.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private WorkflowNode getWorkflowNodeById(ObjectId nodeId) {
        return nodeRepository.findById(nodeId).orElseThrow();
    }

    public void writeWorkflowHistoryEntry(WorkflowItemHistory history) {
        historyRepository.save(history);
    }
}
