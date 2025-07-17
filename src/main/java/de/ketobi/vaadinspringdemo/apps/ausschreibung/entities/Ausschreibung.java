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

    private List<PendingDocumentFileBytes> pendingDocumentsFileBytes = new ArrayList<>();

    public void addPendingDocumentFileBytes(String filename, byte[] fileBytes) {
        this.pendingDocumentsFileBytes.add(new PendingDocumentFileBytes(filename, fileBytes));
    }

}