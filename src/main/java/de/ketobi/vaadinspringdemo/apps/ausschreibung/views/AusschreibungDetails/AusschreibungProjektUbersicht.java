package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;

public class AusschreibungProjektUbersicht extends VerticalLayout {

    @Autowired
    public AusschreibungProjektUbersicht(Ausschreibung ausschreibung, AusschreibungService ausschreibungService) {

        System.out.println("Ausschreibung found: " + ausschreibung.getTitle()); // Initialize the layout and components
                                                                                // for the project overview
        H2 header = new H2("Projektübersicht: Ausschreibung " + ausschreibung.getTitle());
        header.getStyle().set("text-align", "center");
        add(header);

        add(new AusschreibungSummaryGrid(ausschreibung));

        add(new H2("OnePager erstellen"));
        add(new H4(
                "Über den Button 'One-Pager erstellen' kannst du deine Projektinformationen auf einer kompakten Seite sehen."));

        Button onePagerButton = new Button("One-Pager erstellen", event -> {
            // here to put the logic to create a One-Pager for the Ausschreibung
            ausschreibungService.getOnepager(ausschreibung.getUuid())
                    .subscribe(response -> {
                        // Handle the response, e.g., show a notification or redirect
                        getUI().ifPresent(ui -> ui.access(() -> {
                            StreamResource resource = new StreamResource("onepager.pdf",
                                    () -> new ByteArrayInputStream(response));
                            resource.setContentType("application/pdf");

                            Anchor downloadLink = new Anchor(resource, "");
                            downloadLink.getElement().setAttribute("download", true);
                            downloadLink.setId("hidden-download-link");
                            downloadLink.setVisible(true);
                            downloadLink.getStyle().set("display", "block");
                            downloadLink.getStyle().set("width", "0px");
                            downloadLink.getStyle().set("height", "0px");
                            downloadLink.getStyle().set("overflow", "hidden");
                            downloadLink.getStyle().set("position", "absolute");
                            downloadLink.getStyle().set("top", "-1000px");

                            ui.add(downloadLink);
                            ui.getPage().executeJs("document.getElementById('hidden-download-link').click();");

                            Notification.show("One-Pager erstellt: ", 5000,
                                    Notification.Position.TOP_CENTER);
                        }));
                    }, error -> {
                        // Handle error case
                        getUI().ifPresent(ui -> ui.access(() -> {
                            Notification.show("Fehler beim Erstellen des One-Pagers: " + error.getMessage(), 5000,
                                    Notification.Position.TOP_CENTER);
                        }));
                    });
            System.out.println("One-Pager erstellt für Ausschreibung: " + ausschreibung.getTitle());
        });
        onePagerButton.getElement().setAttribute("theme", "neue-ausschreibung");
        add(onePagerButton);
    }

}
