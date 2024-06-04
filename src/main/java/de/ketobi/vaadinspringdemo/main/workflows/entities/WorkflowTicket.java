package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@Builder
public class WorkflowTicket {
    @EqualsAndHashCode.Include
    @Id
    @NonNull
    private ObjectId id;
    private ObjectId workflowId;
    private ObjectId currentResponsibleId;
    private ObjectId currentNodeId;
    private ObjectId entityId;
    private List<ObjectId> siblingIds;
}
