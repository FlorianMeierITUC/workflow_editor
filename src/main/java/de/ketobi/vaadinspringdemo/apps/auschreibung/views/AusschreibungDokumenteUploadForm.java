package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import java.io.InputStream;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.notification.Notification;

import de.ketobi.vaadinspringdemo.apps.auschreibung.components.AusschreibungSummaryGrid;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;

public class AusschreibungDokumenteUploadForm extends VerticalLayout {

    public AusschreibungDokumenteUploadForm(Ausschreibung ausschreibung) {
        setSpacing(true);
        setPadding(true);
        setWidthFull();

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

        add(upload);
    }
}
