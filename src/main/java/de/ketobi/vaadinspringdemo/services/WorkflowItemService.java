package de.ketobi.vaadinspringdemo.services;

import de.ketobi.vaadinspringdemo.entities.*;
import de.ketobi.vaadinspringdemo.repositories.WorkflowItemHistoryRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowItemService {
    private final WorkflowItemHistoryRepository historyRepository;
    private final WorkflowNodeRepository nodeRepository;
    private final UserService userService;

    @Autowired
    public WorkflowItemService(WorkflowItemHistoryRepository historyRepository, WorkflowNodeRepository nodeRepository, UserService userService) {
        this.historyRepository = historyRepository;
        this.nodeRepository = nodeRepository;
        this.userService = userService;
    }

    public WorkflowNode getWorkflowNodeById(ObjectId currentNodeId) {
        return nodeRepository.findById(currentNodeId);
    }

    public List<WorkflowItem> getAllWorkflowItemsAssignedToTheCurrentUser() {
        List<WorkflowItem> workflowItems = new ArrayList<>();
        for(WorkflowTypes type : WorkflowTypes.values()){
            //get all workflow items of the current user
        }
        return null;
    }

    public void startWorkflow(WorkflowItem workflowItem) {
        workflowItem.setWorkflow(workflowItem.getWorkflow());
        if(workflowItem.getWorkflow().getStartNode()==null){
            workflowItem.getWorkflow().setStartNode(nodeRepository.findByIdWorkflowAndType(workflowItem.getWorkflow().getId(), WorkflowNodeTypes.START));
        }
        workflowItem.setCurrentNode(workflowItem.getWorkflow().getStartNode());
        nextNode(workflowItem.getCurrentNode(), workflowItem, null, "Workflow started");
    }

    public void nextNode(WorkflowNode currentNode, WorkflowItem workflowItem, Boolean success, String message) {
        User responsible;
        if(currentNode.getResponsible()==null) {
            responsible = UserService.getSystemUser();
        } else if (currentNode.getResponsible().equals(UserService.getSystemUser().getId())) {
            responsible = UserService.getSystemUser();
        } else {
            responsible = userService.getUserById(currentNode.getResponsible());
        }

        WorkflowItemHistory history = WorkflowItemHistory.builder()
                .workflow(workflowItem.getWorkflow())
                .node(currentNode)
                .item(workflowItem)
                .message(message)
                .responsible(responsible)
                .createdAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
        WorkflowNode nextNode = null;
        switch (currentNode.getType()) {
            case BATCH_DECISION:
            case USER_DECISION:
                if (success) {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_success());
                    workflowItem.setCurrentNode(nextNode);
                } else {
                    nextNode = getWorkflowNodeById(currentNode.getSuccessorNode_failure());
                    workflowItem.setCurrentNode(nextNode);
                }
                break;
            case START:
            case USER_ACTION:
            case BATCH_ACTION:
            case UNION:
                nextNode = getWorkflowNodeById(currentNode.getSuccessorNodes().get(0));
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
                    .workflow(workflowItem.getWorkflow())
                    .node(nextNode)
                    .item(workflowItem)
                    .message("Workflow finished")
                    .responsible(UserService.getSystemUser())
                    .createdAt(LocalDateTime.now())
                    .build();
            historyRepository.save(historyEnd);
        }


    }
}
