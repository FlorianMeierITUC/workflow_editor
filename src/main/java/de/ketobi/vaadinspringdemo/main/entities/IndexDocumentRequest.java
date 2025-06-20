package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;

@Data
public class IndexDocumentRequest {
    private String text;
    private String project_uuid;
    private String document_type;
    private String document_title;
}
