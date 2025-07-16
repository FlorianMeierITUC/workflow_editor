package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import lombok.Data;
import java.util.UUID;

@Data
public class PendingDocument {
    private String filename;
    private String extractedText;
    private UUID documentUuid;

    public PendingDocument(String filename, String extractedText) {
        this.filename = filename;
        this.extractedText = extractedText;
    }

    public PendingDocument(String filename, String extractedText, UUID documentUuid) {
        this.filename = filename;
        this.extractedText = extractedText;
        this.documentUuid = documentUuid;
    }
}
