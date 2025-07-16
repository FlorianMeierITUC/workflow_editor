package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.UUID;

@Data
public class IndexDocumentRequest {
    private String text;
    @JsonProperty("project_uuid")
    private UUID projectUuid;
    @JsonProperty("document_type")
    private String documentType;
    @JsonProperty("document_title")
    private String documentTitle;
}
