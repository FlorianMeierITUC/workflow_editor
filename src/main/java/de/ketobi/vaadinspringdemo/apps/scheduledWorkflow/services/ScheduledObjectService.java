package de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.services;

import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.entities.ScheduledWorkflowEntity;
import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.repositories.ScheduledWorkflowEntityRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledObjectService {
    private final ScheduledWorkflowEntityRepository scheduledWorkflowEntityRepository;

    @Autowired
    public ScheduledObjectService(ScheduledWorkflowEntityRepository scheduledWorkflowEntityRepository){
        this.scheduledWorkflowEntityRepository = scheduledWorkflowEntityRepository;
    }

    public void save(ScheduledWorkflowEntity scheduledObject) {
        scheduledWorkflowEntityRepository.save(scheduledObject);
    }

    public ScheduledWorkflowEntity getById(ObjectId itemId) {
        return scheduledWorkflowEntityRepository.findById(itemId).orElseThrow();
    }
}
