package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import java.io.InputStream;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.notification.Notification;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungActionButtons;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import com.vaadin.flow.component.tabs.Tabs;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;

public class AusschreibungDokumenteUploadForm extends VerticalLayout {

    public AusschreibungDokumenteUploadForm(Ausschreibung ausschreibung, AusschreibungService ausschreibungService, Tabs tabs) {
        setSpacing(true);
        setPadding(true);
        setWidthFull();
        setSizeFull();

        add(new H3("Grundinformations Übersicht"));

        add(new AusschreibungSummaryGrid(ausschreibung));

        // Placeholder for upload
        add(new H3("Dokumenten Upload"));
        add(new H4("Checkliste für ein erfolgreiches Ausschreibungsprojekt"));

        // File upload module
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setWidthFull();
        upload.setDropLabel(new Div(new Text("Datei hierher ziehen oder klicken zum Auswählen")));
        upload.setAcceptedFileTypes(".pdf", ".docx", ".xlsx", ".txt");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            ausschreibung.addDokument(fileName);
            InputStream fileData = buffer.getInputStream();
            Notification.show("Datei hochgeladen: " + fileName, 3000, Notification.Position.MIDDLE);
            // You could now store fileData
        });

        upload.addFailedListener(event -> {
            Notification.show("Fehler beim Hochladen: " + event.getFileName(), 3000, Notification.Position.MIDDLE);
        });

        AusschreibungActionButtons buttonLayout = new AusschreibungActionButtons(
            ausschreibung,
            ausschreibungService,
            tabs,
            () -> {
                // Optional: additional logic after save (e.g., refresh view)
            }
        );

        add(upload);

        add(buttonLayout);
        setFlexGrow(1, upload);
    }
}
