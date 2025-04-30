package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.auschreibung.components.LabeledField;

public class AusschreibungBasicInfoForm extends VerticalLayout {

    private final Ausschreibung ausschreibung;
    private final Tabs tabs;

    public AusschreibungBasicInfoForm(Ausschreibung ausschreibung, AusschreibungService ausschreibungService, Tabs tabs) {
        this.ausschreibung = ausschreibung;
        this.tabs = tabs;

        setPadding(false);
        setSpacing(true);
        setSizeFull();

        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 3) // 3 equal columns
        );


        TextField ausschreibungsNumberField = new TextField();
        TextField ITUCNumberField = new TextField();
        TextField partnerFirmaField = new TextField();
        TextField kundeField = new TextField();
        TextField brancheField = new TextField();
        TextField projectkKontaktField = new TextField();
        TextField projectkKontaktEmailField = new TextField();
        TextField titleField = new TextField();
        TextArea notizenField = new TextArea();

        // Bind values
        ausschreibungsNumberField.addValueChangeListener(e -> ausschreibung.setAusschreibungsNumber(e.getValue()));
        ITUCNumberField.addValueChangeListener(e -> ausschreibung.setITUCNumber(e.getValue()));
        partnerFirmaField.addValueChangeListener(e -> ausschreibung.setPartnerFirma(e.getValue()));
        kundeField.addValueChangeListener(e -> ausschreibung.setKunde(e.getValue()));
        brancheField.addValueChangeListener(e -> ausschreibung.setBranche(e.getValue()));
        projectkKontaktField.addValueChangeListener(e -> ausschreibung.setProjectkKontakt(e.getValue()));
        projectkKontaktEmailField.addValueChangeListener(e -> ausschreibung.setProjectkKontaktEmail(e.getValue()));
        titleField.addValueChangeListener(e -> ausschreibung.setTitel(e.getValue()));
        notizenField.addValueChangeListener(e -> ausschreibung.setNotizen(e.getValue()));

        // Add fields
        form.add(
            new LabeledField("Ausschreibungs Nr.", ausschreibungsNumberField, "123 456 789"),
            new LabeledField("ITUC Nr.", ITUCNumberField, "123 456"),
            new LabeledField("PartnerFirma (Optional)", partnerFirmaField, "-"),

            new LabeledField("Kunde", kundeField, "Volkswagen AG"),
            new LabeledField("Branche", brancheField, "Automobilindustrie")
        );

        form.add(new Div());

        form.add(
            new LabeledField("Projektkontakt (Optional)", projectkKontaktField, "Max Mustermann"),
            new LabeledField("Projektkontakt E-Mail (Optional)", projectkKontaktEmailField, "max.mustermann@vw.de")
        );

        form.add(new Div());


        Component titleComponent = new LabeledField("Titel", titleField, "Projekt Titel");
        form.add(titleComponent);
        form.setColspan(titleComponent, 2);
        form.add(new Div());

        Component notizenComponent = new LabeledField("Kurznotizen", notizenField, "Projekthintergrund, Ziele usw.");
        form.add(notizenComponent);
        form.setColspan(notizenComponent, 2);

        // Action buttons
        Button deleteButton = new Button("Löschen", e -> Notification.show("Noch nicht implementiert"));
        Button cancelButton = new Button("Abbrechen", e -> getUI().ifPresent(ui -> ui.navigate("auschreibung")));
        Button saveButton = new Button("Speichern & Weiter", e -> {
            ausschreibungService.save(ausschreibung);
            tabs.setSelectedIndex(1);
            Notification.show("Gespeichert");
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, cancelButton, saveButton);
        buttonLayout.setWidthFull();
        buttonLayout.getStyle().set("margin-top", "2rem");
        buttonLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);

        add(new H3("Grundinformationen für das Projekt"), form, buttonLayout);
        setFlexGrow(1, form);
    }
}
