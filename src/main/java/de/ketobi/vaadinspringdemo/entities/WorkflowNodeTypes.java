package de.ketobi.vaadinspringdemo.entities;

/**
 * Enum representing the types of workflow nodes.
 * Every node has only one predecessor, except the start node, which has none and the union node which has multiple predecessors.
 */
public enum WorkflowNodeTypes {
    /**
     * User decision node. User decision has two successors, success and failure.
     */
    USER_DECISION("User decision"),

    /**
     * User action node. User action has only one successor.
     */
    USER_ACTION("User action"),

    /**
     * Batch decision node. Batch decision has two successors, success and failure.
     */
    BATCH_DECISION("Batch decision"),

    /**
     * Batch action node. Batch action has only one successor.
     */
    BATCH_ACTION("Batch action"),

    /**
     * OR node. The OR node has one predecessor and can have multiple successors, the successors must be followed by a union node.
     */
    OR("OR"),

    /**
     * AND node. The AND node has one predecessor and can have multiple successors, the successors must be followed by a union node.
     */
    AND("AND"),

    /**
     * Union node. The union node has multiple predecessors and one successor.
     */
    UNION("UNION"),

    /**
     * Start node. The start node has no predecessor and exactly one successor.
     */
    START("START"),

    /**
     * End node. The end node has multiple predecessors and no successor.
     */
    END("END");

    private final String type;

    WorkflowNodeTypes(String type) {
        this.type = type;
    }

    /**
     * Returns the type of the workflow node.
     *
     * @return the type of the workflow node
     */
    public String getType() {
        return type;
    }
}