package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteProjectRequest {
    @JsonProperty("project_uuid")
    private UUID projectUuid;

}
