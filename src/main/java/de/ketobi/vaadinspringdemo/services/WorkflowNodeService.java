package de.ketobi.vaadinspringdemo.services;

import de.ketobi.vaadinspringdemo.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.repositories.WorkflowNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowNodeService {
    private final WorkflowNodeRepository nodeRepository;

    @Autowired
    public WorkflowNodeService(WorkflowNodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    public void saveWorkflowNode(WorkflowNode node) {
        nodeRepository.save(node);
    }
}
