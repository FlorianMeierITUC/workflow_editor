package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
<<<<<<< HEAD
import lombok.Data
=======
import lombok.Data;
>>>>>>> e3afe61 (correction of the add button css)

@Data
@Document("ausschreibungen")
public class Ausschreibung {
    private UUID uuid;
    @Id
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

    private List<String> dokumente = new ArrayList<>();

    public void addDokument(String filename) {
        this.dokumente.add(filename);
    }
}