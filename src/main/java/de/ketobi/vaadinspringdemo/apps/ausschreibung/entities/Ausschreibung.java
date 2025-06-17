package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data

import lombok.Data;

@Data
@Document("ausschreibungen")
public class Ausschreibung {
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
<<<<<<< HEAD:src/main/java/de/ketobi/vaadinspringdemo/apps/ausschreibung/entities/Ausschreibung.java
    private LocalDate date;
    private boolean favorite;
    private boolean archived;
    private String status;

    private List<String> dokumente = new ArrayList<>();

    public void addDokument(String filename) {
        this.dokumente.add(filename);
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isArchived() {
        return archived;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
=======
>>>>>>> 92c89a0 (Update src/main/java/de/ketobi/vaadinspringdemo/apps/auschreibung/entities/Ausschreibung.java):src/main/java/de/ketobi/vaadinspringdemo/apps/auschreibung/entities/Ausschreibung.java
}
