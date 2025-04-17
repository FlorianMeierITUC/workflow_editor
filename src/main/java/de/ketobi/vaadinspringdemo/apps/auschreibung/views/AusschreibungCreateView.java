package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;

@Route("auschreibung/create")
@PageTitle("Neue Ausschreibung")
public class AusschreibungCreateView extends VerticalLayout {

    private final AusschreibungService ausschreibungService;
    private final AusschreibungFormData formData = new AusschreibungFormData();
    private Tabs tabs;

    public AusschreibungCreateView(AusschreibungService ausschreibungService) {
        this.ausschreibungService = ausschreibungService;

        setWidth("800px");
        setPadding(true);
        setSpacing(true);
        setSizeFull();

        H2 pageTitle = new H2("Neue Ausschreibung anlegen");

        Tab step1 = new Tab("Grundinformationen");
        Tab step2 = new Tab("Dokumente Upload");
        Tab step3 = new Tab("Zusammenfassung");

        tabs = new Tabs(step1, step2, step3);
        tabs.setWidthFull();

        Div content = new Div();
        content.setWidthFull();
        content.setSizeFull();

        // Load initial content
        content.add(getInfoForm());

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (event.getSelectedTab().equals(step1)) {
                content.add(getInfoForm());
            } else if (event.getSelectedTab().equals(step2)) {
                content.add(getDokumenteUploadForm());
            } else {
                content.add(getPublishStep());
            }
        });

        add(pageTitle, tabs, content);
        setFlexGrow(1, content);
    }

    private Component getInfoForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setSizeFull();

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3)); // 3 columns

        // Fields
        TextField ausschreibungsNumberField = new TextField();
        ausschreibungsNumberField.setWidthFull();
        ausschreibungsNumberField.addClassName("form-field");

        TextField ITUCNumberField = new TextField();
        ITUCNumberField.setWidthFull();
        ITUCNumberField.addClassName("form-field");

        TextField partnerFirmaField = new TextField();
        partnerFirmaField.setWidthFull();
        partnerFirmaField.addClassName("form-field");

        TextField kundeField = new TextField();
        kundeField.setWidthFull();
        kundeField.addClassName("form-field");

        TextField brancheField = new TextField();
        brancheField.setWidthFull();
        brancheField.addClassName("form-field");

        TextField projectkKontaktField = new TextField();
        projectkKontaktField.setWidthFull();
        projectkKontaktField.addClassName("form-field");

        TextField titleField = new TextField();
        titleField.setWidthFull();
        titleField.addClassName("form-field");

        TextArea notizenField = new TextArea();
        notizenField.setWidthFull();
        notizenField.setHeight("120px");
        notizenField.addClassName("form-field");

        TextField projectkKontaktEmailField = new TextField();
        projectkKontaktEmailField.setWidthFull();
        projectkKontaktEmailField.addClassName("form-field");

        // Update the formData (simplified for demo)
        ausschreibungsNumberField.addValueChangeListener(e -> formData.setAusschreibungsNumber(e.getValue()));
        ITUCNumberField.addValueChangeListener(e -> formData.setItucNumber(e.getValue()));

        H3 titleStep1 = new H3("Grundinformationen für das Projekt");
        titleStep1.getStyle().set("margin-bottom", "1rem");

        // Form rows
        form.add(
            createLabeledField("Ausschreibungs Nr.", ausschreibungsNumberField),
            createLabeledField("ITUC Nr.", ITUCNumberField),
            createLabeledField("PartnerFirma (Optional)", partnerFirmaField)
        );

        form.add(
            createLabeledField("Kunde", kundeField),
            createLabeledField("Branche", brancheField)
        );

        form.add(new Div());
        form.add(
            createLabeledField("Projektkontakt (Optional)", projectkKontaktField),
            createLabeledField("Projektkontakt E-Mail (Optional)", projectkKontaktEmailField)
        );

        form.add(new Div());
        Component titelFieldComponent = createLabeledField("Titel", titleField);
        form.add(titelFieldComponent);
        form.setColspan(titelFieldComponent, 2);
        form.add(new Div());

        Component notizenComponent = createLabeledField("Notizen", notizenField);
        form.add(notizenComponent);
        form.setColspan(notizenComponent, 2);
        form.add(new Div());

        // Buttons
        Button deleteButton = new Button("Löschen", e -> Notification.show("Nicht implementiert"));
        Button cancelButton = new Button("Abbrechen", e -> getUI().ifPresent(ui -> ui.navigate("auschreibung")));
        Button saveButton = new Button("Speichern & Weiter", e -> {
            Ausschreibung a = new Ausschreibung(formData.getAusschreibungsNumber(), formData.getItucNumber());
            ausschreibungService.save(a);
            tabs.setSelectedIndex(1);
            Notification.show("Gespeichert");
        });

        HorizontalLayout leftButtons = new HorizontalLayout(deleteButton);
        leftButtons.setWidthFull();
        leftButtons.setJustifyContentMode(JustifyContentMode.START);

        HorizontalLayout rightButtons = new HorizontalLayout(cancelButton, saveButton);
        rightButtons.setJustifyContentMode(JustifyContentMode.END);
        rightButtons.setSpacing(true);

        HorizontalLayout buttonRow = new HorizontalLayout(leftButtons, rightButtons);
        buttonRow.setWidthFull();
        buttonRow.setJustifyContentMode(JustifyContentMode.BETWEEN);
        buttonRow.getStyle().set("margin-top", "2rem");

        layout.add(titleStep1, form, buttonRow);
        layout.setFlexGrow(1, form);
        layout.setAlignSelf(Alignment.END, buttonRow);

        return layout;
    }

    private Component createLabeledField(String label, Component field) {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSpacing(false);
        wrapper.setPadding(false);
        wrapper.setMargin(false);
        wrapper.setWidthFull();

        H3 fieldLabel = new H3(label);
        fieldLabel.getStyle().set("margin", "0 0 0.25em 0").set("font-size", "1rem");

        wrapper.add(fieldLabel, field);
        return wrapper;
    }

    private Component getDokumenteUploadForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H2("Schritt 2: Kriterien definieren"));
        layout.add(new TextArea("Kriterien (noch leer)"));
        return layout;
    }

    private Component getPublishStep() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H2("Schritt 3: Überprüfen & Veröffentlichen"));

        Button saveBtn = new Button("Speichern & Veröffentlichen", e -> {
            Ausschreibung a = new Ausschreibung(formData.getAusschreibungsNumber(), formData.getItucNumber());
            ausschreibungService.save(a);
            Notification.show("Ausschreibung gespeichert!");
            getUI().ifPresent(ui -> ui.navigate("auschreibung"));
        });

        layout.add(saveBtn);
        return layout;
    }

    private static class AusschreibungFormData {
        private String ausschreibungsNumber;
        private String itucNumber;

        public String getAusschreibungsNumber() {
            return ausschreibungsNumber;
        }

        public void setAusschreibungsNumber(String ausschreibungsNumber) {
            this.ausschreibungsNumber = ausschreibungsNumber;
        }

        public String getItucNumber() {
            return itucNumber;
        }

        public void setItucNumber(String itucNumber) {
            this.itucNumber = itucNumber;
        }
    }
}
