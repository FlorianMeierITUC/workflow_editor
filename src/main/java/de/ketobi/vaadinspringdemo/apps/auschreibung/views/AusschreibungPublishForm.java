package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.apps.auschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;

public class AusschreibungPublishForm extends VerticalLayout {

    public AusschreibungPublishForm(AusschreibungService ausschreibungService, Ausschreibung ausschreibung) {
        setPadding(true);
        setSpacing(true);

        add(new AusschreibungSummaryGrid(ausschreibung));

        // Document list section
        if (!ausschreibung.getDokumente().isEmpty()) {
            add(new H3("Dokumenten Upload"));
            add(new H4("Checkliste für ein erfolgreiches Ausschreibungsprojekt"));

            UnorderedList fileList = new UnorderedList();
            fileList.getStyle().set("padding-left", "1.5rem");

            ausschreibung.getDokumente().forEach(name ->
                fileList.add(new ListItem(name))
            );

            add(fileList);
        } else {
            add(new H4("Keine Dokumente hochgeladen."));
        }
    }
}
