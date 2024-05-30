package de.ketobi.vaadinspringdemo.apps.workflows.entities;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;

public enum WorkflowTypes {
    ORDER_WORKFLOW("Order Workflow", Order.class);

    private final String name;
    private final Class<? extends WorkflowItem> entity;

    WorkflowTypes(String name, Class<? extends WorkflowItem> entity) {
        this.name = name;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public Class<? extends WorkflowItem> getEntity() {
        return entity;
    }
}
