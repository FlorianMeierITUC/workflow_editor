package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.UUID;

@Data
public class RetrieveDocumentsRequest {
    private String question;
    @JsonProperty("project_uuid")
    private UUID projectUuid;
    private String keywords;

}
