package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.ketobi.vaadinspringdemo.apps.auschreibung.components.AusschreibungActionButtons;
import de.ketobi.vaadinspringdemo.apps.auschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;
import com.vaadin.flow.component.tabs.Tabs;

public class AusschreibungPublishForm extends VerticalLayout {

    public AusschreibungPublishForm(Ausschreibung ausschreibung, AusschreibungService ausschreibungService, Tabs tabs) {
        setPadding(true);
        setSpacing(true);
        setSizeFull(); // Make the full height of the view

        // Container for all upper content
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(true);
        contentLayout.setWidthFull();

        contentLayout.add(new AusschreibungSummaryGrid(ausschreibung));

        // Document list section
        if (!ausschreibung.getDokumente().isEmpty()) {
            contentLayout.add(new H3("Dokumenten Upload"));
            contentLayout.add(new H4("Checkliste für ein erfolgreiches Ausschreibungsprojekt"));

            UnorderedList fileList = new UnorderedList();
            fileList.getStyle().set("padding-left", "1.5rem");

            ausschreibung.getDokumente().forEach(name ->
                fileList.add(new ListItem(name))
            );

            contentLayout.add(fileList);
        } else {
            contentLayout.add(new H4("Keine Dokumente hochgeladen."));
        }

        AusschreibungActionButtons buttonLayout = new AusschreibungActionButtons(
            ausschreibung,
            ausschreibungService,
            tabs,
            () -> {
                // Optional: logic after save
            }
        );

        add(contentLayout, buttonLayout);
        setFlexGrow(1, contentLayout); // Ensure content grows and buttons stay at bottom
    }
}
