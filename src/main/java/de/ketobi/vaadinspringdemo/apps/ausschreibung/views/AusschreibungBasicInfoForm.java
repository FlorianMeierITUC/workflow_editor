package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.AusschreibungActionButtons;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.LabeledField;

public class AusschreibungBasicInfoForm extends VerticalLayout {

    private final Binder<Ausschreibung> binder = new Binder<>(Ausschreibung.class);
    private final Ausschreibung ausschreibung;
    private final Tabs tabs;

    private final TextField ausschreibungsNumberField = new TextField();
    private final TextField ITUCNumberField = new TextField();
    private final TextField partnerFirmaField = new TextField();
    private final TextField kundeField = new TextField();
    private final TextField brancheField = new TextField();
    private final TextField projectKontaktField = new TextField();
    private final TextField projectKontaktEmailField = new TextField();
    private final TextField titleField = new TextField();
    private final TextArea notizenField = new TextArea();

    public AusschreibungBasicInfoForm(Ausschreibung ausschreibung,
                                      AusschreibungService ausschreibungService,
                                      Tabs tabs) {
        this.ausschreibung = ausschreibung;
        this.tabs = tabs;

        setPadding(false);
        setSpacing(true);
        setSizeFull();

        // Bind fields to bean properties
        binder.forField(ausschreibungsNumberField)
              .bind(Ausschreibung::getAusschreibungsNumber, Ausschreibung::setAusschreibungsNumber);
        binder.forField(ITUCNumberField)
              .bind(Ausschreibung::getITUCNumber, Ausschreibung::setITUCNumber);
        binder.forField(partnerFirmaField)
              .bind(Ausschreibung::getPartnerFirma, Ausschreibung::setPartnerFirma);
        binder.forField(kundeField)
              .bind(Ausschreibung::getKunde, Ausschreibung::setKunde);
        binder.forField(brancheField)
              .bind(Ausschreibung::getBranche, Ausschreibung::setBranche);
        binder.forField(projectKontaktField)
              .bind(Ausschreibung::getProjectKontakt, Ausschreibung::setProjectKontakt);
        binder.forField(projectKontaktEmailField)
              .bind(Ausschreibung::getProjectKontaktEmail, Ausschreibung::setProjectKontaktEmail);
        binder.forField(titleField)
              .bind(Ausschreibung::getTitle, Ausschreibung::setTitle);
        binder.forField(notizenField)
              .bind(Ausschreibung::getNotizen, Ausschreibung::setNotizen);
		
        // Bind bean two-way: populate fields and update bean on change
        binder.setBean(ausschreibung);

// Build form layout
        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));

        form.add(
            new LabeledField("Ausschreibungs Nr.", ausschreibungsNumberField, "123 456 789"),
            new LabeledField("ITUC Nr.", ITUCNumberField, "123 456"),
            new LabeledField("PartnerFirma (Optional)", partnerFirmaField, "-"),
            new LabeledField("Kunde", kundeField, "Volkswagen AG"),
            new LabeledField("Branche", brancheField, "Automobilindustrie"),
            new Div(),
            new LabeledField("Projektkontakt (Optional)", projectKontaktField, "Max Mustermann"),
            new LabeledField("Projektkontakt E-Mail (Optional)", projectKontaktEmailField, "max.mustermann@vw.de"),
            new Div()
        );

        Component titleComponent = new LabeledField("Title", titleField, "Projekt Titel");
        form.add(titleComponent);
        form.setColspan(titleComponent, 2);
        form.add(new Div());

        notizenField.setWidthFull();
        notizenField.setHeight("200px");
        Component notizenComponent = new LabeledField(
            "Kurznotizen", notizenField,
            "In dieses Feld können projektspezifische Informationen eingetragen werden..."
        );
        form.add(notizenComponent);
        form.setColspan(notizenComponent, 2);

        // Action buttons with save+next callback
        AusschreibungActionButtons buttonLayout = new AusschreibungActionButtons(
            ausschreibung,
            ausschreibungService,
            tabs,
            () -> {
                try {
                    binder.writeBean(ausschreibung);
                    ausschreibungService.save(ausschreibung);
                    Notification.show("Basic info saved");
                    tabs.setSelectedIndex(1);
                } catch (ValidationException e) {
                    Notification.show("Please fix validation errors");
                }
            }
        );

        H3 sectionTitle = new H3("Grundinformationen für das Projekt");
        sectionTitle.getStyle().set("margin-top", "1em");

        add(sectionTitle, form, buttonLayout);
        setFlexGrow(1, form);
    }
}
