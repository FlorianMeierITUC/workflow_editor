package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexDocumentResponse {
   private UUID document_uuid;
    private UUID document_version_uuid;
    private List<String> chunks;
}



