package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class Document {

    @JsonProperty("document_uuid")
    private UUID documentUuid;

    private String id;

    @JsonProperty("file_name")
    private String documentTitle;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    // Getters and setters
    public UUID getDocumentUuid() {
        return documentUuid;
    }

    public void setDocumentUuid(UUID documentUuid) {
        this.documentUuid = documentUuid;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
