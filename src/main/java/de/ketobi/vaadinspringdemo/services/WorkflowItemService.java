package de.ketobi.vaadinspringdemo.services;

import de.ketobi.vaadinspringdemo.entities.*;
import de.ketobi.vaadinspringdemo.repositories.WorkflowItemHistoryRepository;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WorkflowItemService {
    private final WorkflowItemHistoryRepository historyRepository;
    private final WorkflowNodeRepository nodeRepository;

    @Autowired
    public WorkflowItemService(WorkflowItemHistoryRepository historyRepository, WorkflowNodeRepository nodeRepository) {
        this.historyRepository = historyRepository;
        this.nodeRepository = nodeRepository;
    }

    public WorkflowNode getWorkflowNodeById(ObjectId currentNodeId) {
        return nodeRepository.findById(currentNodeId);
    }

    public void startWorkflow(WorkflowItem workflowItem) {
        workflowItem.setWorkflow(workflowItem.getWorkflow());
        workflowItem.setCurrentNode(workflowItem.getWorkflow().getStartNode());
        WorkflowItemHistory history = WorkflowItemHistory.builder()
                .workflow(workflowItem.getWorkflow())
                .node(workflowItem.getCurrentNode())
                .item(workflowItem)
                .message("Workflow started")
                .responsible(User.getSystemUser())
                .createdAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);
        nextNode(workflowItem, null);
    }

    public void nextNode(WorkflowItem workflowItem, Boolean success){
        //switch for the workflow node types except the start node
        WorkflowNode currentNode = workflowItem.getCurrentNode();
        switch (currentNode.getType()) {
            case END -> {
                //do nothing
            }
            case USER_DECISION, BATCH_DECISION -> {
                if (success) {

                } else {

                }
            }
            case USER_ACTION, BATCH_ACTION -> {

            }
            case OR, AND -> {
                //do nothing for now
            }
            case UNION -> {
                //do nothing so far
            }
            default -> throw new IllegalStateException("Unexpected value: " + currentNode.getType());
        }
    }
}
