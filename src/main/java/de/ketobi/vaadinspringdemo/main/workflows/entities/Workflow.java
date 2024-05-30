package de.ketobi.vaadinspringdemo.main.workflows.entities;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {
    @EqualsAndHashCode.Include
    @Id
    @NonNull
    ObjectId id;

    @EqualsAndHashCode.Include
    @NonNull
    String name;

    @NonNull
    WorkflowNode startNode;

    String description;

    boolean active;

    User createdBy;

    Long schedule;

}
