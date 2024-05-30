package de.ketobi.vaadinspringdemo.main.ui.navigation.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class NavigationTarget {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    ObjectId id;

    @EqualsAndHashCode.Include
    @NonNull
    ObjectId idFolder;

    @EqualsAndHashCode.Include
    @NonNull
    String label;

    @NonNull
    Integer index;

    @NonNull
    String view;
}