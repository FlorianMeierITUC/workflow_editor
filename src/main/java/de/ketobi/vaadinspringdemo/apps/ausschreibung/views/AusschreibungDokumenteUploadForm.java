package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.notification.Notification;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungActionButtons;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import com.vaadin.flow.component.tabs.Tabs;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;

public class AusschreibungDokumenteUploadForm extends VerticalLayout {

    private void handleFileUpload(SucceededEvent event, Ausschreibung ausschreibung, MemoryBuffer buffer) {
        String fileName = event.getFileName();
        try (InputStream inputStream = buffer.getInputStream()) {
            byte[] fileBytes = inputStream.readAllBytes();
            ausschreibung.addPendingDocumentFileBytes(fileName, fileBytes);
        } catch (IOException e) {
            Notification.show("Fehler beim Lesen der Datei: " + e.getMessage(), 5000,
                    Notification.Position.TOP_CENTER);
        }
    }

    public AusschreibungDokumenteUploadForm(Ausschreibung ausschreibung, AusschreibungService ausschreibungService,
            Tabs tabs) {
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
        upload.setMaxFileSize(50 * 1024 * 1024); // 50 MB
        upload.setMaxFiles(100);

        upload.addSucceededListener(event -> {
            handleFileUpload(event, ausschreibung, buffer);
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
                });

        add(upload);

        add(buttonLayout);
        setFlexGrow(1, upload);
    }
}
