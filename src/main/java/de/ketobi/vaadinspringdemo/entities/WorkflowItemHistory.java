package de.ketobi.vaadinspringdemo.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkflowItemHistory {
    private Workflow workflow;
    private WorkflowNode node;
    private WorkflowItem item;
    private String message;
    private User responsible;
    private LocalDateTime createdAt;
}
