package de.ketobi.vaadinspringdemo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {
    @Getter
    @Setter
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    String id;

    @Getter
    @Setter
    @Indexed(unique = true)
    @EqualsAndHashCode.Include
    @NonNull
    String name;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    boolean done;

    @Getter
    @Setter
    String createdBy;

    @Getter
    @Setter
    LocalDateTime createdAt;

    @Getter
    @Setter
    LocalDateTime doneAt;


}
