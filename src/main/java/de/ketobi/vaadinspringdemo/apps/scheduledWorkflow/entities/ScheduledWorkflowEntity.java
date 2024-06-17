package de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.entities;

import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledWorkflowEntity implements WorkflowEntity {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private ObjectId id;
    private String name;
    private String description;
    private String resultScheduledBatchAction;
    private LocalDateTime createdAt;
    private ObjectId createdBy;

    @Override
    public WorkflowTypes getWorkflowType() {
        return WorkflowTypes.SCHEDULED_WORKFLOW;
    }

    @Override
    public WorkflowEntity getScheduledWorkflowStartEntity() {
        ScheduledWorkflowEntity entity = new ScheduledWorkflowEntity();
        entity.setId(ObjectId.get());
        entity.setName("Scheduled Workflow Object");
        entity.setDescription("This is a scheduled workflow object");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setCreatedBy(UserService.getSystemUser().getId());
        return entity;
    }
}
