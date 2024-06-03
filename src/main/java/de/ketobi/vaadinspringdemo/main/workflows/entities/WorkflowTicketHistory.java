package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class WorkflowTicketHistory {
    private ObjectId id;
    private ObjectId ticketId;
    private String workflowName;
    private String nodeTitle;
    private String entityName;
    private String message;
    private String responsibleUser;
    private LocalDateTime createdAt;
}
