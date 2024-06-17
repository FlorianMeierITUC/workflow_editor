package de.ketobi.vaadinspringdemo.main.workflows.entities;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.apps.scheduledWorkflow.entities.ScheduledWorkflowEntity;
import org.bson.types.ObjectId;

public enum WorkflowTypes {
    ORDER_WORKFLOW("Order Workflow", Order.class, new ObjectId("664dd9c649a7d57f42c0a1e4")),
    DEMO_WORKFLOW("Test", DemoObject.class, new ObjectId("664d996ae4d3377dc1403c41")),
    SCHEDULED_WORKFLOW("Scheduled Workflow", ScheduledWorkflowEntity.class, new ObjectId("666966be31d9cd79d67771d7"));

    private final String name;
    private final Class<? extends WorkflowEntity> entity;
    private final ObjectId id;

    WorkflowTypes(String name, Class<? extends WorkflowEntity> entity, ObjectId id) {
        this.name = name;
        this.entity = entity;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Class<? extends WorkflowEntity> getEntity() {
        return entity;
    }

    public ObjectId getId() {
        return id;
    }

    public static WorkflowTypes fromId(ObjectId id) {
        for (WorkflowTypes type : WorkflowTypes.values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

    public static WorkflowTypes fromName(String name) {
        for (WorkflowTypes type : WorkflowTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
