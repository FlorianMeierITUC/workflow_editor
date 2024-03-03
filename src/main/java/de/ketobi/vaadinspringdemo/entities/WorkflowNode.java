package de.ketobi.vaadinspringdemo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class WorkflowNode {
    @EqualsAndHashCode.Include
    @Id
    @Getter
    @Setter
    @NonNull
    String id;

    @Getter
    @Setter
    @NonNull
    String idWorkflow;

    @Getter
    @Setter
    @NonNull
    String title;

    @Getter
    @Setter
    @NonNull
    String type;

    @Getter
    @Setter
    String executorClass;

    @Getter
    @Setter
    String responsible;

    @Getter
    @Setter
    ArrayList<String> precessorNodes;

    @Getter
    @Setter
    ArrayList<String> successorNodes;

}
