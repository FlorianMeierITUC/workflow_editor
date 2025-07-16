package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.UUID;

@Data
public class RetrieveOnepagerDocuments {
    @JsonProperty("project_uuid")
    private UUID projectUuid;

    public RetrieveOnepagerDocuments(UUID projectUuid) {
        this.projectUuid = projectUuid;
    }

}
