package de.ketobi.vaadinspringdemo.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    @EqualsAndHashCode.Include
    @NonNull
    String name;

    String description;

    boolean active;

    String createdBy;

    Long schedule;

}
