package de.ketobi.vaadinspringdemo.apps.demo.entities;

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
public class DemoObject implements WorkflowEntity {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private ObjectId id;
    private String name;
    private String description;
    private String resultUserAction1;
    private String resultUserAction2;
    private String resultBatchAction;
    private String resultBatchAction2;
    private LocalDateTime createdAt;
    private ObjectId createdBy;

    @Override
    public WorkflowTypes getWorkflowType() {
        return WorkflowTypes.DEMO_WORKFLOW;
    }

    @Override
    public WorkflowEntity getScheduledWorkflowStartEntity() {
        //logic to create the object
        return this;
    }
}
