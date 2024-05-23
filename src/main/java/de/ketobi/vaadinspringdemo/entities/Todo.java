package de.ketobi.vaadinspringdemo.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    ObjectId id;

    @EqualsAndHashCode.Include
    @NonNull
    String name;

    String description;

    boolean done;

    ObjectId createdBy;

    LocalDateTime createdAt;

    LocalDateTime doneAt;
}
