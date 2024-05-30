package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
    WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public Workflow findByName(String name) {
        return workflowRepository.findByName(name).orElseThrow();
    }

    public Workflow findById(ObjectId workflowId) {
        return workflowRepository.findById(workflowId).orElseThrow();
    }
}
