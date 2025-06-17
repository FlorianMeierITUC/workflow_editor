package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;

public class AusschreibungProjektUbersicht extends VerticalLayout {

    public AusschreibungProjektUbersicht(Ausschreibung ausschreibung) {

System.out.println("Ausschreibung found: " + ausschreibung.getTitle());        // Initialize the layout and components for the project overview
        H2 header = new H2("Projektübersicht: Ausschreibung " + ausschreibung.getTitle());
        header.getStyle().set("text-align", "center");
        add(header);

        add(new AusschreibungSummaryGrid(ausschreibung));

        add(new H2("OnePager erstellen"));
        add(new H4("Über den Button 'One-Pager erstellen' kannst du deine Projektinformationen auf einer kompakten Seite sehen."));

        Button onePagerButton = new Button("One-Pager erstellen", event -> {
            // here to put the logic to create a One-Pager for the Ausschreibung
            System.out.println("One-Pager erstellt für Ausschreibung: " + ausschreibung.getTitle());
        });
        onePagerButton.getElement().setAttribute("theme", "neue-ausschreibung");
        add(onePagerButton);
    }
    
}
