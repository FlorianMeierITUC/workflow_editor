package de.ketobi.vaadinspringdemo.apps.auschreibung.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;

public class AusschreibungActionButtons extends HorizontalLayout {

    public AusschreibungActionButtons(Ausschreibung ausschreibung, AusschreibungService service, Tabs tabs, Runnable afterSave) {
        setWidthFull();
        getStyle().set("margin-top", "2rem");

        // Delete Button
        Button deleteButton = new Button("Löschen...", e -> Notification.show("Noch nicht implementiert"));
        deleteButton.getStyle()
                .set("background-color", "hsla(3, 85%, 48%, 1)")
                .set("color", "white")
                .set("border-radius", "2px");

        // Cancel Button
        Button cancelButton = new Button("Abbrechen", e -> getUI().ifPresent(ui -> ui.navigate("auschreibung")));
        cancelButton.getStyle().set("border-radius", "2px");

        // Save Button
        Button saveButton = new Button("Speichern & Weiter", e -> {
            if (tabs.getSelectedIndex() == 2) {
                showConfirmationDialog(ausschreibung, service);
            } else {
                service.save(ausschreibung);
                tabs.setSelectedIndex(tabs.getSelectedIndex() + 1);
                Notification.show("Gespeichert");
                if (afterSave != null) afterSave.run();
            }
        });
        saveButton.getStyle()
                .set("background-color", "hsla(145, 72%, 30%, 1)")
                .set("color", "white")
                .set("border-radius", "2px");

        // Layouts for alignment
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
        dialog.setHeaderTitle("Das Ausschreibungsprojekt " + ausschreibung.getTitel() + " wurde erfolgreich angelegt.");
        // Make dialog half-width and center its content
        dialog.setWidth("null");
        dialog.setHeight("null");
        dialog.setDraggable(true);
        dialog.setResizable(false);
    
        // Dialog content
        VerticalLayout content = new VerticalLayout(
            new Span("Das Ausschreibungsprojekt wurde erfolgreich angelegt, und nun beginnt die spannende Phase der Detailarbeit."),
            new Span("Der KI-Chat Bot steht bereit, um maßgeschneiderte Unterstützung zu bieten."),
            new Span("Sie können Ihre Inhalte anpassen, ergänzen, ändern, Dateien verwalten und neue hochladen."),
            new Span("Sie haben nun die Möglichkeit, direkt ins Projekt einzutauchen oder zur Home-Oberfläche zurückzukehren.")
        );
        content.setAlignItems(Alignment.CENTER); // centers content horizontally
        content.getStyle()
            .set("text-align", "center")
            .set("padding", "1rem")
            .set("font-weight", "normal");
        content.setPadding(true);
        content.setSpacing(true);
        
    
        dialog.add(content);
    
        // Buttons
        Button confirm = new Button("Zurück zur Home", ev -> {
            service.save(ausschreibung);
            Notification.show("Ausschreibung veröffentlicht!");
            dialog.close();
            getUI().ifPresent(ui -> ui.navigate("auschreibung"));
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
        buttonLayout.setSpacing(true); // Adds spacing between buttons
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.END); // Align to right

        dialog.add(buttonLayout);
      
        dialog.open();
    }
    
}
