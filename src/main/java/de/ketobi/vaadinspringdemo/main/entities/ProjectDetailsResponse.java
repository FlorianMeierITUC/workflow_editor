package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectDetailsResponse {
    private UUID uuid;
    @JsonProperty("ituc_id")
    private String itucId;
    @JsonProperty("tender_id")
    private String tenderId;
    @JsonProperty("due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("crreated_by")
    private String createdBy;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
