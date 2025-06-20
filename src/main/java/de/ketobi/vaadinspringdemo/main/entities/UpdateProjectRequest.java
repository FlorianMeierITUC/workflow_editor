package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateProjectRequest extends CreateProjectRequest {
    @JsonProperty("project_uuid")
    private UUID projectUuid; // The UUID of the project to update
}
