package de.ketobi.vaadinspringdemo.main.workflows.entities;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public interface WorkflowEntity {
    WorkflowTypes getWorkflowType();
    ObjectId getId();
    String getName();
    ObjectId getCreatedBy();
    LocalDateTime getCreatedAt();
    WorkflowEntity getScheduledWorkflowStartEntity();
}
