package de.ketobi.vaadinspringdemo.apps.demo.entities;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItem;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@Builder
public class DemoObject extends WorkflowItem {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private ObjectId id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private ObjectId createdBy;

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public void saveItem() {
        saveToDatabase(this);
    }
}
