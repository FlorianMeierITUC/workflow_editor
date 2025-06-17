package de.ketobi.vaadinspringdemo.apps.auschreibung.entities;

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
}
