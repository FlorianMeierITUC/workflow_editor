package de.ketobi.vaadinspringdemo.apps.ausschreibung.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.PendingDocument;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.main.entities.ProjectResponse;
import reactor.core.publisher.Mono;

public class AusschreibungActionButtons extends HorizontalLayout {
    private final Div spinner = new Div();

    public AusschreibungActionButtons(Ausschreibung ausschreibung, AusschreibungService ausschreibungService,
                                       Tabs tabs, Runnable afterSave) {
        setWidthFull();
        getStyle().set("margin-top", "2rem");

        spinner.setText("Lade...");
        spinner.getStyle().set("position", "fixed").set("top", "0").set("left", "0").set("width", "100%")
                .set("height", "100%").set("background", "rgba(0, 0, 0, 0.4)").set("color", "white")
                .set("display", "flex").set("align-items", "center").set("justify-content", "center")
                .set("z-index", "9999").set("font-size", "1.5rem").set("visibility", "hidden");

        add(this.spinner);

        // Delete Button
        Button deleteButton = new Button("Löschen...", e -> {
            if (ausschreibung.getUuid() == null) {
                Notification.show("Ausschreibung kann man nicht loeschen");
            } else {
                UI ui = UI.getCurrent();
                ausschreibungService.deleteProject(ausschreibung).subscribe(response -> {
                    if (ui != null && ui.isAttached()) {
                        ui.access(() -> {
                            Notification.show("Ausschreibung gelöscht");
                            ui.navigate("ausschreibung");
                        });
                    }
                }, error -> {
                    if (ui != null && ui.isAttached()) {
                        ui.access(() -> {
                            Notification.show("Fehler beim Löschen der Ausschreibung: " + error.getMessage(),
                                    5000, Notification.Position.MIDDLE);
                        });
                    }
                });
            }
        });
        deleteButton.getStyle().set("background-color", "hsla(3, 85%, 48%, 1)").set("color", "white")
                .set("border-radius", "2px");

        // Cancel Button
        Button cancelButton = new Button("Abbrechen",
                e -> getUI().ifPresent(ui -> ui.navigate("ausschreibung")));
        cancelButton.getStyle().set("border-radius", "2px");

        // Save Button
        Button saveButton = new Button("Speichern & Weiter", e -> {
            UI ui = UI.getCurrent();
            if (tabs.getSelectedIndex() == 2) {
                Mono<ProjectResponse> requestMono;
                if (ausschreibung.getUuid() != null) {
                    requestMono = ausschreibungService.updateProject(ausschreibung);
                } else {
                    requestMono = ausschreibungService.createProject(ausschreibung);
                }

                requestMono.doOnTerminate(() -> {
                    if (ui != null && ui.isAttached()) {
                        ui.access(() -> spinner.getStyle().set("visibility", "hidden"));
                    }
                }).subscribe(result -> {
                    if (ausschreibung.getUuid() == null) {
                        ausschreibung.setUuid(result.getProjectUuid());
                        System.out.println("Ausschreibung UUID: " + ausschreibung.getUuid());
                    }

                    for (PendingDocument doc : ausschreibung.getPendingDocuments()) {
                        String fileName = doc.getFilename();
                        String extractedText = doc.getExtractedText();
                        ausschreibungService.indexDocument(extractedText, fileName, ausschreibung)
                                .subscribe(indexResponse -> {
                                    System.out.println("Indexed document: "
                                            + indexResponse.getDocument_uuid());
                                }, error -> {
                                    System.err.println("Error indexing document: "
                                            + error.getMessage());
                                });
                    }

                    if (ui != null && ui.isAttached()) {
                        ui.access(() -> showConfirmationDialog(ausschreibung, ausschreibungService));
                    }
                }, error -> {
                    if (ui != null && ui.isAttached()) {
                        ui.access(() -> {
                            Notification.show("Error: " + error.getMessage(), 5000,
                                    Notification.Position.MIDDLE);
                        });
                    }
                });

            } else {
                tabs.setSelectedIndex(tabs.getSelectedIndex() + 1);
                Notification.show("Gespeichert");
                if (afterSave != null) afterSave.run();
            }
        });

        saveButton.getStyle().set("background-color", "hsla(145, 72%, 30%, 1)").set("color", "white")
                .set("border-radius", "2px");

        HorizontalLayout leftSide = new HorizontalLayout(deleteButton);
        leftSide.setWidthFull();
        leftSide.setJustifyContentMode(JustifyContentMode.START);

        HorizontalLayout rightSide = new HorizontalLayout(cancelButton, saveButton);
        rightSide.setWidthFull();
        rightSide.setJustifyContentMode(JustifyContentMode.END);

        add(leftSide, rightSide);
    }

    private void showConfirmationDialog(Ausschreibung ausschreibung, AusschreibungService service) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Das Ausschreibungsprojekt " + ausschreibung.getTitle() + " wurde erfolgreich angelegt.");
        dialog.setWidth("null");
        dialog.setHeight("null");
        dialog.setDraggable(true);
        dialog.setResizable(false);

        VerticalLayout content = new VerticalLayout(
                new Span("Das Ausschreibungsprojekt wurde erfolgreich angelegt, und nun beginnt die spannende Phase der Detailarbeit."),
                new Span("Der KI-Chat Bot steht bereit, um maßgeschneiderte Unterstützung zu bieten."),
                new Span("Sie können Ihre Inhalte anpassen, ergänzen, ändern, Dateien verwalten und neue hochladen."),
                new Span("Sie haben nun die Möglichkeit, direkt ins Projekt einzutauchen oder zur Home-Oberfläche zurückzukehren."));
        content.setAlignItems(Alignment.CENTER);
        content.getStyle()
                .set("text-align", "center")
                .set("padding", "1rem")
                .set("font-weight", "normal");
        content.setPadding(true);
        content.setSpacing(true);

        dialog.add(content);

        Button confirm = new Button("Zurück zur Home", ev -> {
            Notification.show("Ausschreibung veröffentlicht!");
            dialog.close();
            getUI().ifPresent(ui -> ui.navigate("ausschreibung"));
        });
        confirm.getStyle()
                .set("background-color", "hsla(0, 0%, 85%, 1)")
                .set("color", "hsla(216, 34%, 21%, 1)")
                .set("border-radius", "3px")
                .set("border", "none");

        Button cancel = new Button("Ausschreibungsprojekt bearbeiten", ev -> dialog.close());
        cancel.getStyle()
                .set("background-color", "var(--ituc-semantic-primary)")
                .set("color", "white")
                .set("border-radius", "3px")
                .set("border", "none");

        HorizontalLayout buttonLayout = new HorizontalLayout(confirm, cancel);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(buttonLayout);
        dialog.open();
    }
}
