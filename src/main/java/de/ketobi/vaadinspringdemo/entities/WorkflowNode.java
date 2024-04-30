package de.ketobi.vaadinspringdemo.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
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

    String executorClass;

    String responsible;

    ArrayList<ObjectId> predecessorNodes;

    ArrayList<ObjectId> successorNodes;
}
