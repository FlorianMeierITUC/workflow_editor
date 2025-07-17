package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Document;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.main.entities.ExtractTextResponse;

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

        // File upload component
        //FIXME: change it to component + reuse it and refactoring
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setWidthFull();
        upload.setDropLabel(new Div(new Text("Datei hierher ziehen oder klicken zum Auswählen")));
        upload.setAcceptedFileTypes(".pdf", ".docx", ".xlsx", ".txt");
        upload.setMaxFileSize(50 * 1024 * 1024); // 50 MB
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            System.out.println("Trying to upload file: " + event.getFileName());
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            try {
                byte[] fileBytes = inputStream.readAllBytes();

                // Extract text from the uploaded file
                service.extractAusschreibungText(fileBytes, fileName)
                    .flatMap((ExtractTextResponse extractResponse) -> {
                        String extractedText = extractResponse.getText();

                        // Now use indexDocument to save this new document into the database
                        return service.indexDocument(extractedText, fileName, ausschreibung);
                    })
                    .subscribe(
                        indexedDocumentResponse -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                            Notification.show("Dokument erfolgreich hochgeladen und indexiert", 3000, Notification.Position.TOP_CENTER);
                            UI.getCurrent().getPage().reload();
                        })),
                        error -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                            error.printStackTrace();
                            Notification.show("Fehler beim Indexieren des Dokuments", 5000, Notification.Position.TOP_CENTER);
                        }))
                    );

            } catch (IOException e) {
                Notification.show("Fehler beim Lesen der Datei: " + e.getMessage(), 5000,
                        Notification.Position.TOP_CENTER);
            }
        });

        upload.addFailedListener(event -> {
            Notification.show("Fehler beim Hochladen: " + event.getFileName(), 3000, Notification.Position.MIDDLE);
        });

        add(upload);
        setFlexGrow(1, upload);

        // List existing documents
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
                            MemoryBuffer bufferUpdate = new MemoryBuffer();
                            Upload uploadUpdate = new Upload(bufferUpdate);
                            uploadUpdate.setAcceptedFileTypes(".pdf", ".txt");
                            uploadUpdate.setMaxFiles(1);
                            uploadUpdate.setDropLabel(new Span("Neues Dokument hier ablegen oder klicken"));
                            uploadUpdate.setWidthFull();

                            uploadUpdate.addSucceededListener(event -> {
                                String filename = event.getFileName();
                                InputStream inputStream = bufferUpdate.getInputStream();

                                try {
                                    byte[] fileBytes = inputStream.readAllBytes();

                                    service.extractAusschreibungText(fileBytes, filename)
                                        .flatMap(extractResponse -> {
                                            String extractedText = extractResponse.getText();

                                            return service.indexDocument(
                                                extractedText,
                                                filename,
                                                ausschreibung
                                            );
                                        })
                                        .subscribe(
                                            indexed -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                                Notification.show("Dokument aktualisiert und indexiert", 3000, Notification.Position.TOP_CENTER);
                                                remove(upload); // remove uploader after success
                                                UI.getCurrent().getPage().reload();
                                            })),
                                            error -> getUI().ifPresent(innerUi -> innerUi.access(() -> {
                                                error.printStackTrace();
                                                Notification.show("Fehler beim Indexieren des Dokuments", 5000, Notification.Position.TOP_CENTER);
                                            }))
                                        );

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Notification.show("Fehler beim Lesen der Datei", 5000, Notification.Position.TOP_CENTER);
                                }
                            });

                            add(uploadUpdate); // show upload inline
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
