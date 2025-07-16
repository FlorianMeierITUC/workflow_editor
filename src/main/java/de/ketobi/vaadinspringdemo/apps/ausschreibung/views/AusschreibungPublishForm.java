package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungActionButtons;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
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
        if (!ausschreibung.getPendingDocuments().isEmpty()) {
            contentLayout.add(new H3("Dokumenten Upload"));
            contentLayout.add(new H4("Checkliste für ein erfolgreiches Ausschreibungsprojekt"));

            UnorderedList fileList = new UnorderedList();
            fileList.getStyle().set("padding-left", "1.5rem");

            ausschreibung.getPendingDocuments().forEach(item -> fileList.add(new ListItem(item.getFilename())));

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
                });

        add(contentLayout, buttonLayout);
        setFlexGrow(1, contentLayout); 
    }
}
