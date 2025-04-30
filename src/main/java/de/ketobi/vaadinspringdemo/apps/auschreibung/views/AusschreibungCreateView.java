// File: AusschreibungCreateView.java
package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.auschreibung.views.AusschreibungBasicInfoForm;
import de.ketobi.vaadinspringdemo.apps.auschreibung.views.AusschreibungDokumenteUploadForm;
import de.ketobi.vaadinspringdemo.apps.auschreibung.views.AusschreibungPublishForm;

@Route("auschreibung/create")
@PageTitle("Neue Ausschreibung")
public class AusschreibungCreateView extends VerticalLayout {

    private final AusschreibungService ausschreibungService;
    private final Ausschreibung formData = new Ausschreibung();
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

        content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (event.getSelectedTab().equals(step1)) {
                content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));
            } else if (event.getSelectedTab().equals(step2)) {
                content.add(new AusschreibungDokumenteUploadForm(formData));
            } else {
                content.add(new AusschreibungPublishForm( ausschreibungService, formData));
            }
        });

        add(pageTitle, tabs, content);
        setFlexGrow(1, content);
    }
}
