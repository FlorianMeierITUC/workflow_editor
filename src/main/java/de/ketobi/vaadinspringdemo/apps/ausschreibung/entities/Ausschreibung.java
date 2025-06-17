package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data

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

    public Ausschreibung() {}
    public Ausschreibung(String title, String beschreibung) {
        this.title = title;
        this.beschreibung = beschreibung;
    }

    public Ausschreibung(String title, LocalDate date, boolean archived) {
        this.title    = title;
        this.date     = date;
        this.archived = archived;
        this.favorite = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public String getAusschreibungsNumber() {
        return ausschreibungsNumber;
    }

    public void setAusschreibungsNumber(String ausschreibungsNumber) {
        this.ausschreibungsNumber = ausschreibungsNumber;
    }

    public String getITUCNumber() {
        return ITUCNumber;
    }

    public void setITUCNumber(String ITUCNumber) {
        this.ITUCNumber = ITUCNumber;
    }

    public String getPartnerFirma() {
        return partnerFirma;
    }

    public void setPartnerFirma(String partnerFirma) {
        this.partnerFirma = partnerFirma;
    }

    public String getKunde() {
        return kunde;
    }

    public void setKunde(String kunde) {
        this.kunde = kunde;
    }

    public String getBranche() {
        return branche;
    }

    public void setBranche(String branche) {
        this.branche = branche;
    }

    public String getProjectKontakt() {
        return projectKontakt;
    }

    public void setProjectKontakt(String projectKontakt) {
        this.projectKontakt = projectKontakt;
    }

    public String getProjectKontaktEmail() {
        return projectKontaktEmail;
    }

    public void setProjectKontaktEmail(String projectKontaktEmail) {
        this.projectKontaktEmail = projectKontaktEmail;
    }

    public String getNotizen() {
        return notizen;
    }

    public void setNotizen(String notizen) {
        this.notizen = notizen;
    }

    private List<String> dokumente = new ArrayList<>();

    public List<String> getDokumente() {
        return dokumente;
    }

    public void setDokumente(List<String> dokumente) {
        this.dokumente = dokumente;
    }

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
