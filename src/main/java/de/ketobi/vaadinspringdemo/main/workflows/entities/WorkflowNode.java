package de.ketobi.vaadinspringdemo.main.workflows.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;

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

    Boolean isValid = true;

    ObjectId responsible;
    List<ObjectId> predecessorNodes = new ArrayList<>();
    List<ObjectId> successorNodes = new ArrayList<>();
    ObjectId successorNode_success;
    ObjectId successorNode_failure;

    public String toString() {
        return "WorkflowNode{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                '}';
    }

    public String getClassName() {
        return id.toHexString();
    }

    public boolean hasPredecessorNode() {
        return !predecessorNodes.isEmpty();
    }

    public boolean hasSuccessorNode() {
        return !successorNodes.isEmpty();
    }

    public boolean validateNode() {
        EnumSet<WorkflowNodeTypes> singlePredecessorTypes = EnumSet.of(WorkflowNodeTypes.START, WorkflowNodeTypes.END,
                WorkflowNodeTypes.UNION);
        EnumSet<WorkflowNodeTypes> singleSuccessorTypes = EnumSet.of(WorkflowNodeTypes.USER_ACTION,
                WorkflowNodeTypes.BATCH_ACTION);
        EnumSet<WorkflowNodeTypes> responsibleRequiredTypes = EnumSet.of(WorkflowNodeTypes.USER_ACTION,
                WorkflowNodeTypes.USER_DECISION);
        EnumSet<WorkflowNodeTypes> decisionTypes = EnumSet.of(WorkflowNodeTypes.USER_DECISION,
                WorkflowNodeTypes.BATCH_DECISION);

        if (!singlePredecessorTypes.contains(type) && predecessorNodes.size() != 1) {
            return false;
        }
        if (singleSuccessorTypes.contains(type) && successorNodes.size() != 1) {
            return false;
        }
        if (responsibleRequiredTypes.contains(type) && responsible == null) {
            return false;
        }
        if (decisionTypes.contains(type) && (successorNode_success == null || successorNode_failure == null)) {
            return false;
        }
        return true;
    }
}
