package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@Builder
public class WorkflowTicket {
    @EqualsAndHashCode.Include
    @Id
    @NonNull
    private ObjectId id;
    @NonNull
    private ObjectId workflowId;
    @NonNull
    private ObjectId currentResponsibleId;
    @NonNull
    private ObjectId currentNodeId;
    @NonNull
    private ObjectId entityId;
    @NonNull
    private LocalDateTime createdAt;

    private List<ObjectId> siblingIds;
}
