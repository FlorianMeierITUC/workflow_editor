package de.ketobi.vaadinspringdemo.apps.auschreibung.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
            service.save(ausschreibung);
            tabs.setSelectedIndex(tabs.getSelectedIndex() + 1);
            Notification.show("Gespeichert");
            if (afterSave != null) afterSave.run();
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
}
