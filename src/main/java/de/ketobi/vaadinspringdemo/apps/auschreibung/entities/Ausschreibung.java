package de.ketobi.vaadinspringdemo.apps.auschreibung.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ausschreibungen")
public class Ausschreibung {
    @Id
    private String id;
    private String titel;
    private String beschreibung;
    private String ausschreibungsNumber;
    private String ITUCNumber;
    private String partnerFirma;
    private String kunde;
    private String branche;
    private String projectkKontakt;
    private String projectkKontaktEmail;
    private String title;
    private String notizen;

    
    public Ausschreibung() {}
    public Ausschreibung(String titel, String beschreibung) {
        this.titel = titel;
        this.beschreibung = beschreibung;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitel() { return titel; }
    public void setTitel(String titel) { this.titel = titel; }

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

    public String getProjectkKontakt() {
        return projectkKontakt;
    }

    public void setProjectkKontakt(String projectkKontakt) {
        this.projectkKontakt = projectkKontakt;
    }

    public String getProjectkKontaktEmail() {
        return projectkKontaktEmail;
    }

    public void setProjectkKontaktEmail(String projectkKontaktEmail) {
        this.projectkKontaktEmail = projectkKontaktEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
