package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes.END;
import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes.START;

@Service
public class WorkflowNodeService {
    private final WorkflowNodeRepository nodeRepository;

    @Autowired
    public WorkflowNodeService(WorkflowNodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void save(WorkflowNode workflowNode) {
        nodeRepository.save(workflowNode);
    }

    public WorkflowNode getById(String workflowNodeId) {
        return nodeRepository.findById(new ObjectId(workflowNodeId)).orElseThrow();
    }

    public WorkflowNode getById(ObjectId workflowNodeId) {
        return nodeRepository.findById(workflowNodeId).orElseThrow();
    }

    public ArrayList<WorkflowNode> getAll(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId));
    }

    public ArrayList<WorkflowNode> getAllWithoutEnd(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream().filter(n -> n.getType() != END)
                .collect(Collectors.toList()));
    }

    public ArrayList<WorkflowNode> getAllWithoutStart(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream().filter(n -> n.getType() != START)
                .collect(Collectors.toList()));
    }

    public ArrayList<WorkflowNode> getAllWithoutStartAndEnd(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream()
                .filter(node -> !node.getType().equals(WorkflowNodeTypes.START)
                        && !node.getType().equals(WorkflowNodeTypes.END))
                .collect(Collectors.toList()));
    }

    public ArrayList<WorkflowNode> getAllBatchNodes(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream()
                .filter(node -> node.getType().equals(WorkflowNodeTypes.BATCH_ACTION)
                        || node.getType().equals(WorkflowNodeTypes.BATCH_DECISION))
                .collect(Collectors.toList()));
    }

    public WorkflowNode getStartNode(ObjectId workflowId) {
        return nodeRepository.findByIdWorkflowAndType(workflowId, START).orElseThrow();
    }

    public WorkflowNode getEndNode(ObjectId workflowId) {
        return nodeRepository.findByIdWorkflowAndType(workflowId, END).orElseThrow();
    }

    public void deleteAllFromWorkflow(ObjectId workflowId) {
        nodeRepository.deleteAll(nodeRepository.findByIdWorkflow(workflowId));
    }
}
