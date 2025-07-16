package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class Document {

    @JsonProperty("document_uuid")
    private UUID documentUuid;

    private String id;

    @JsonProperty("file_name")
    private String documentTitle;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
