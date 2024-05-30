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

    public void save(WorkflowNode node) {
        nodeRepository.save(node);
    }

    public WorkflowNode getById(String id) {
        return nodeRepository.findById(new ObjectId(id)).orElseThrow();
    }

    public WorkflowNode getById(ObjectId id){
        return nodeRepository.findById(id).orElseThrow();
    }

    public ArrayList<WorkflowNode> getAll(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId));
    }

    public ArrayList<WorkflowNode> getAllWithoutEnd(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream().filter(n -> n.getType()!= END).collect(Collectors.toList()));
    }

    public ArrayList<WorkflowNode> getAllWithoutStart(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream().filter(n -> n.getType()!= START).collect(Collectors.toList()));
    }

    public ArrayList<WorkflowNode> getAllWithoutStartAndEnd(ObjectId workflowId) {
        return new ArrayList<>(nodeRepository.findByIdWorkflow(workflowId).stream().filter(node -> !node.getType().equals(WorkflowNodeTypes.START) && !node.getType().equals(WorkflowNodeTypes.END)).collect(Collectors.toList()));
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
