package de.ketobi.vaadinspringdemo.entities;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class WorkflowItemHistory {
    private ObjectId id;
    private Workflow workflow;
    private WorkflowNode node;
    private WorkflowItem item;
    private String message;
    private User responsible;
    private LocalDateTime createdAt;
}
