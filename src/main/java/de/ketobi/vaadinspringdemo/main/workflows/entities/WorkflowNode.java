package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WorkflowNode {
    @EqualsAndHashCode.Include
    @Id
    @NonNull
    ObjectId id;

    @NonNull
    ObjectId idWorkflow;

    @NonNull
    String title;

    @NonNull
    WorkflowNodeTypes type;

    ObjectId responsible;
    List<ObjectId> predecessorNodes = new ArrayList<>();
    List<ObjectId> successorNodes = new ArrayList<>();
    ObjectId successorNode_success;
    ObjectId successorNode_failure;

    public String toString(){
        return "WorkflowNode{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }

    public String getClassName() {
        return id.toHexString();
    }
}
