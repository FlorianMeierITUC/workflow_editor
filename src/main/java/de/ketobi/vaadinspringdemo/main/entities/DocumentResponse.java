package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;

@Data
public class DocumentResponse {
    @JsonProperty("combined_text")
    String combinedText;
    List<String> documents;

}
