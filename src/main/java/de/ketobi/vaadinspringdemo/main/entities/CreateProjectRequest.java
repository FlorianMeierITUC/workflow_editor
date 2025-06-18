package de.ketobi.vaadinspringdemo.main.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

//ToDo: Add missing fields -> fields also missing in indexing service
@Data
public class CreateProjectRequest {
    private String name;
    @JsonProperty("ituc_id")
    private String itucId;
    @JsonProperty("tender_id")
    private String tenderId;
    @JsonProperty("due_date")
    // FIXME: Not very clean and quite hacky
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;
    @JsonProperty("display_name")
    private String displayName;
}
