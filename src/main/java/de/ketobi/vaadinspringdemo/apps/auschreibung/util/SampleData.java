package de.ketobi.vaadinspringdemo.apps.auschreibung.util;

import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SampleData {

    public static List<Ausschreibung> createArchivedAusschreibungen() {
        Ausschreibung a1 = new Ausschreibung();
        a1.setId("1");
        a1.setAusschreibungsNumber("AUS-2025-001");
        a1.setITUCNumber("ITUC-2025-045");
        a1.setTitle("Metro Tunnel Extension Munich");
        a1.setDate(LocalDate.of(2023, 7, 10));
        a1.setKunde("Stadt München");
        a1.setBranche("Verkehr");
        a1.setStatus("Completed");
        a1.setArchived(true);
        a1.setFavorite(true);

        Ausschreibung a2 = new Ausschreibung();
        a2.setId("2");
        a2.setAusschreibungsNumber("AUS-2025-002");
        a2.setITUCNumber("ITUC-2025-032");
        a2.setTitle("Hospital IT System Upgrade Hamburg");
        a2.setDate(LocalDate.of(2022, 11, 22));
        a2.setKunde("Hamburg University Hospital");
        a2.setBranche("Gesundheitswesen");
        a2.setStatus("Awarded");
        a2.setArchived(true);
        a2.setFavorite(false);

        Ausschreibung a3 = new Ausschreibung();
        a3.setId("3");
        a3.setAusschreibungsNumber("AUS-2025-003");
        a3.setITUCNumber("ITUC-2025-078");
        a3.setTitle("Public Library Renovation Berlin");
        a3.setDate(LocalDate.of(2024, 1, 15));
        a3.setKunde("Berliner Öffentliche Bibliotheken");
        a3.setBranche("Bildung");
        a3.setStatus("Completed");
        a3.setArchived(true);
        a3.setFavorite(true);

        Ausschreibung a4 = new Ausschreibung();
        a4.setId("4");
        a4.setAusschreibungsNumber("AUS-2025-004");
        a4.setITUCNumber("ITUC-2025-056");
        a4.setTitle("Park Landscaping Cologne");
        a4.setDate(LocalDate.of(2023, 5, 5));
        a4.setKunde("Stadt Köln");
        a4.setBranche("Freizeit");
        a4.setStatus("Cancelled");
        a4.setArchived(true);
        a4.setFavorite(false);

        Ausschreibung a5 = new Ausschreibung();
        a5.setId("5");
        a5.setAusschreibungsNumber("AUS-2025-005");
        a5.setITUCNumber("ITUC-2025-089");
        a5.setTitle("School Expansion Frankfurt");
        a5.setDate(LocalDate.of(2022, 3, 30));
        a5.setKunde("Schulamt Frankfurt");
        a5.setBranche("Bildung");
        a5.setStatus("Under Review");
        a5.setArchived(true);
        a5.setFavorite(false);

        return Arrays.asList(a1, a2, a3, a4, a5);
    }
}
