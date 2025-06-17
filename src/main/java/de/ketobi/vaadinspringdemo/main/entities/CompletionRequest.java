package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletionRequest {

    private String model;
    private String prompt;
    private int max_tokens;
    private float temperature;
}
