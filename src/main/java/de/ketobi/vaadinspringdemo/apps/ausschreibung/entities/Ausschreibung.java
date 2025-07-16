package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class Ausschreibung {
    private UUID uuid;
    private String id;
    private String title;
    private String beschreibung;
    private String ausschreibungsNumber;
    private String ITUCNumber;
    private String partnerFirma;
    private String kunde;
    private String branche;
    private String projectKontakt;
    private String projectKontaktEmail;
    private String notizen;
    private LocalDateTime date;
    private boolean favorite;
    private boolean archived;
    private String status;

    private List<PendingDocument> pendingDocuments = new ArrayList<>();

    public void addPendingDocument(String filename, String extractedText) {
        this.pendingDocuments.add(new PendingDocument(filename, extractedText));
    }

    public void updatePendingDocument(String filename, String extractedText, UUID documentUuid) {
        this.pendingDocuments.add(new PendingDocument(filename, extractedText, documentUuid));
    }
}