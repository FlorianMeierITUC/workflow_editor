package de.ketobi.vaadinspringdemo.apps.auschreibung.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ausschreibungen")
public class Ausschreibung {
    @Id
    private String id;
    private String titel;
    private String beschreibung;

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
}
