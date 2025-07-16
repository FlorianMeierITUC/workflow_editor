package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDocumentRequest {
    @JsonProperty("text")
    private String text;
    @JsonProperty("project_uuid")
    private UUID projectUuid;
    @JsonProperty("document_type")
    private String documentType;
    @JsonProperty("document_title")
    private String documentTitle;
    @JsonProperty("document_uuid")
    private UUID documentUuid;
}
