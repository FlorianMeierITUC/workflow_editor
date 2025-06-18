package de.ketobi.vaadinspringdemo.apps.auschreibung.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document("ausschreibungen")
public class Ausschreibung {
    @Id
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

    private List<String> dokumente = new ArrayList<>();

    public void addDokument(String filename) {
        this.dokumente.add(filename);
    }

}
