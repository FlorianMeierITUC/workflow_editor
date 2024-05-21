package de.ketobi.vaadinspringdemo.entities;

public enum WorkflowTypes {
    ORDER_WORKFLOW("Order Workflow");

    private final String name;

    WorkflowTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
