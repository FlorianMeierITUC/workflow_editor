package de.ketobi.vaadinspringdemo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order extends WorkflowItem{
    @Getter
    @Setter
    @Id
    @EqualsAndHashCode.Include
    @NonNull
    String id;

    @Getter
    @Setter
    @NonNull
    String item;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    String reason;

    @Getter
    @Setter
    String supplier;

    @Getter
    @Setter
    BigDecimal price;

    @Getter
    @Setter
    boolean deleted;

    @Getter
    @Setter
    LocalDateTime createdAt;

    @Getter
    @Setter
    String createdBy;
}
