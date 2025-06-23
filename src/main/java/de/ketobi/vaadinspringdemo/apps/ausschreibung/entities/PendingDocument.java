package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import lombok.Data;

@Data
public class PendingDocument {
    private String filename;
    private String extractedText;

    public PendingDocument(String filename, String extractedText) {
        this.filename = filename;
        this.extractedText = extractedText;
    }

}
