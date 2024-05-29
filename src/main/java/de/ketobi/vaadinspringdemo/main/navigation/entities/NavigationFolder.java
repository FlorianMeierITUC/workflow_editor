package de.ketobi.vaadinspringdemo.main.navigation.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NavigationFolder{
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    ObjectId id;

    @EqualsAndHashCode.Include
    @NonNull
    String label;

    @NonNull
    Integer index;
}