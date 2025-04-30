package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;

public class AusschreibungPublishForm extends VerticalLayout {

    public AusschreibungPublishForm(AusschreibungService ausschreibungService, Ausschreibung ausschreibung) {
        add(new H2("Schritt 3: Überprüfen & Veröffentlichen"));

        Button publishButton = new Button("Speichern & Veröffentlichen", e -> {
            ausschreibungService.save(ausschreibung);
            Notification.show("Ausschreibung erfolgreich gespeichert!");
            getUI().ifPresent(ui -> ui.navigate("auschreibung"));
        });

        add(publishButton);
    }
}
