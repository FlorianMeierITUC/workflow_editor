package de.ketobi.vaadinspringdemo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Todo {

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

    public Todo(){

    }
}
