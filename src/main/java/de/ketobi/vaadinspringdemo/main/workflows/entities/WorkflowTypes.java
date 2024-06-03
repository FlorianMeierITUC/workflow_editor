package de.ketobi.vaadinspringdemo.main.workflows.entities;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import org.bson.types.ObjectId;

public enum WorkflowTypes {
    ORDER_WORKFLOW("Order Workflow", Order.class, new ObjectId("664dd9c649a7d57f42c0a1e4")),
    DEMO_WORKFLOW("Test", DemoObject.class, new ObjectId("664d996ae4d3377dc1403c41"));

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
}
