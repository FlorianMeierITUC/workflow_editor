package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public void addDokument(String filename) {
        this.dokumente.add(filename);
    }
>>>>>>> e3afe61 (correction of the add button css)
}
