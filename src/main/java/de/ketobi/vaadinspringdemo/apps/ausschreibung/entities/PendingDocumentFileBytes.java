package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import lombok.Data;

@Data
public class PendingDocumentFileBytes {
    private String fileName;
    private byte[] fileBytes;

    public PendingDocumentFileBytes(String fileName, byte[] fileBytes) {
        this.fileName = fileName;
        this.fileBytes = fileBytes;
    }

}
