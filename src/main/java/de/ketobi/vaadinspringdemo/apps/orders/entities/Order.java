package de.ketobi.vaadinspringdemo.apps.orders.entities;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
@Builder
public class Order implements WorkflowEntity {
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    private ObjectId id;

    @NonNull
    private String item;

    private String description;

    private String reason;

    private String supplier;

    private BigDecimal price;

    private String orderNumber;

    private boolean deleted;

    private LocalDateTime createdAt;

    private ObjectId createdBy;

    private boolean itemReceived;


    @Override
    public WorkflowTypes getWorkflowType() {
        return WorkflowTypes.ORDER_WORKFLOW;
    }

    @Override
    public String getName() {
        return item;
    }
}
