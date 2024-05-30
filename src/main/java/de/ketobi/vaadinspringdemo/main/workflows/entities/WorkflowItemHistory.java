package de.ketobi.vaadinspringdemo.main.workflows.entities;

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
    private ObjectId itemId;
    private String workflowName;
    private String nodeTitle;
    private String itemTitle;
    private String message;
    private String responsibleUser;
    private LocalDateTime createdAt;
}
