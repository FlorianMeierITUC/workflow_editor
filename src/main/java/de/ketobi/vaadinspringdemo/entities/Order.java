package de.ketobi.vaadinspringdemo.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class Order extends WorkflowItem{
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

    private boolean deleted;

    private LocalDateTime createdAt;

    private ObjectId createdBy;

}
