package de.ketobi.vaadinspringdemo.entities;

public enum WorkflowNodeTypes {
    USER_DECISION("User decision"),
    USER_ACTION("User action"),
    BATCH_DECISION("Batch decision"),
    BATCH_ACTION("Batch action"),
    OR("OR"),
    AND("AND"),
    UNION("UNION"),
    START("START"),
    END("END");

    private final String type;

    WorkflowNodeTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
