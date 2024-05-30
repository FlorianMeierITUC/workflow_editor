package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes.START;

@Service
public class WorkflowService {
    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository nodeRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository, WorkflowNodeRepository nodeRepository) {
        this.workflowRepository = workflowRepository;
        this.nodeRepository = nodeRepository;
    }

    public void save(Workflow workflow) {
        workflowRepository.save(workflow);
    }

    public void delete(Workflow workflow) {
        workflowRepository.delete(workflow);
    }

    public ArrayList<Workflow> findAll() {
        return new ArrayList<>(workflowRepository.findAll());
    }

    public Workflow getByName(String name) {
        return workflowRepository.findByName(name).orElseThrow();
    }

    public Workflow getById(ObjectId workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow();
    }

    public Workflow getById(String workflowId) {
        return workflowRepository.findById(new ObjectId(workflowId)).orElseThrow();
    }

    public WorkflowNode getStartNode(ObjectId workflowId) {
        return nodeRepository.findByIdWorkflowAndType(workflowId, START).orElseThrow();
    }
}
