package de.ketobi.vaadinspringdemo.main.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.Map;

@Data
public class OnepagerDocumentsResponse {
    @JsonProperty("onepager_documents")
    private Map<Integer, String> onepagerDocuments;

}
