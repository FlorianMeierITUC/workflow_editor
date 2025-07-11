package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Document;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class AusschreibungDokManagement extends VerticalLayout {

    public AusschreibungDokManagement(Ausschreibung ausschreibung, AusschreibungService service) {
        setSpacing(true);
        setPadding(true);

        System.out.println("DokManagement initialized");

        if (ausschreibung == null || ausschreibung.getUuid() == null) {
            add(new Span("Die Ausschreibung wurde noch nicht gespeichert. Dokumentenmanagement ist erst danach verfügbar."));
            return;
        }

        add(new H2("Dokumenten Management für: " + ausschreibung.getTitle()));

        Button uploadBtn = new Button("Dokument hochladen", event -> {
            Notification.show("Upload-Logik noch nicht implementiert", 3000, Notification.Position.TOP_CENTER);
        });
        add(uploadBtn);

        service.listProjectDocuments(ausschreibung.getUuid())
            .subscribe(response -> {
                List<Document> documents = response.getDocuments();
                getUI().ifPresent(ui -> ui.access(() -> {
                    if (documents == null || documents.isEmpty()) {
                        add(new Span("Keine Dokumente gefunden."));
                        return;
                    }

                    add(new H2("Vorhandene Dokumente:"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

                    for (Document doc : documents) {
                        String title = doc.getDocumentTitle() != null ? doc.getDocumentTitle() : "Unbenannt";
                        String createdAt = doc.getCreatedAt() != null
                            ? doc.getCreatedAt().format(formatter)
                            : "Unbekanntes Datum";

                        Span docEntry = new Span("📄 " + title + " (erstellt am: " + createdAt + ")");
                        Button deleteBtn = new Button("Löschen");
                        Button updateBtn = new Button("Aktualisieren");

                        HorizontalLayout row = new HorizontalLayout(docEntry, deleteBtn, updateBtn);
                        add(row);

                        // Delete handler
                        deleteBtn.addClickListener(click -> {
                            UUID documentUuid = doc.getDocumentUuid();
                            service.deleteDocument(documentUuid).subscribe(
                                unused -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                    remove(row);
                                    Notification.show("Dokument gelöscht", 3000, Notification.Position.TOP_CENTER);
                                })),
                                error -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                    error.printStackTrace();
                                    Notification.show("Fehler beim Löschen des Dokuments", 5000, Notification.Position.TOP_CENTER);
                                }))
                            );
                        });

                        // Update handler - upload replacement document
                        updateBtn.addClickListener(click -> {
                            MemoryBuffer buffer = new MemoryBuffer();
                            Upload upload = new Upload(buffer);
                            upload.setAcceptedFileTypes(".pdf", ".txt");
                            upload.setMaxFiles(1);
                            upload.setDropLabel(new Span("Neues Dokument hier ablegen oder klicken"));
                            upload.setWidthFull();

                            upload.addSucceededListener(event -> {
                                String filename = event.getFileName();
                                InputStream inputStream = buffer.getInputStream();

                                try {
                                    byte[] fileBytes = inputStream.readAllBytes();

                                    service.extractAusschreibungText(fileBytes, filename)
                                        .flatMap(extractResponse -> {
                                            String extractedText = extractResponse.getText();

                                            return service.updateDocument(
                                                extractedText,
                                                filename,
                                                ausschreibung,
                                                doc.getDocumentUuid()
                                            );
                                        })
                                        .subscribe(
                                            updated -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                                Notification.show("Dokument aktualisiert", 3000, Notification.Position.TOP_CENTER);
                                                remove(upload); // remove uploader after success
                                            })),
                                            error -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                                error.printStackTrace();
                                                Notification.show("Fehler beim Aktualisieren des Dokuments", 5000, Notification.Position.TOP_CENTER);
                                            }))
                                        );

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Notification.show("Fehler beim Lesen der Datei", 5000, Notification.Position.TOP_CENTER);
                                }
                            });

                            add(upload); // show upload inline
                        });
                    }
                }));
            }, error -> {
                getUI().ifPresent(ui -> ui.access(() -> {
                    error.printStackTrace();
                    add(new Span("Fehler beim Abrufen der Dokumente: " + error.getMessage()));
                    Notification.show("Fehler beim Laden der Dokumente.", 5000, Notification.Position.TOP_CENTER);
                }));
            });
    }
}
