package de.ketobi.vaadinspringdemo.entities;

import com.vaadin.flow.server.VaadinSession;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    ObjectId id;

    @EqualsAndHashCode.Include
    @NonNull
    String name;

    @NonNull
    String email;

    @NonNull
    String password;
}
