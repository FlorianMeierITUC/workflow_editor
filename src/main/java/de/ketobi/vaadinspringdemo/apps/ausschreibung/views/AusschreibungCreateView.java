package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import de.ketobi.vaadinspringdemo.main.ui.MainLayout;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Route(value = "ausschreibung/create", layout = MainLayout.class)
@PageTitle("Create / Edit Ausschreibung")
public class AusschreibungCreateView extends VerticalLayout implements HasUrlParameter<String> {

    private final AusschreibungService ausschreibungService;
    private final Mapper mapper;
    private Ausschreibung formData;
    private final H2 pageTitle;
    private final Tabs tabs;
    private final Div content;

    public AusschreibungCreateView(AusschreibungService ausschreibungService, Mapper mapper) {
        this.ausschreibungService = ausschreibungService;
        this.mapper = mapper;

        setWidth("800px");
        setPadding(true);
        setSpacing(true);
        setSizeFull();

        // Title will be updated in setParameter
        pageTitle = new H2("Create / Edit Ausschreibung");

        // Setup steps
        Tab step1 = new Tab("Grundinformationen");
        Tab step2 = new Tab("Dokumente Upload");
        Tab step3 = new Tab("Zusammenfassung");

        tabs = new Tabs(step1, step2, step3);
        tabs.setWidthFull();

        content = new Div();
        content.setWidthFull();
        content.setSizeFull();

        // Handle tab changes
        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            if (event.getSelectedTab().equals(step1)) {
                content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));
            } else if (event.getSelectedTab().equals(step2)) {
                content.add(new AusschreibungDokumenteUploadForm(formData, ausschreibungService, tabs));
                step1.getElement().getStyle().set("color", "green");
                step1.setLabel("Grundinformationen ✔");
            } else {
                content.add(new AusschreibungPublishForm(formData, ausschreibungService, tabs));
                step2.getElement().getStyle().set("color", "green");
                step2.setLabel("Dokumente Upload ✔");
            }
        });

        add(pageTitle, tabs, content);
        setFlexGrow(1, content);
    }

    /** Handle optional :id parameter for edit vs create */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String uuid) {

        if (uuid != null) {
            UI ui = UI.getCurrent();
            ausschreibungService.getProjectDetails(UUID.fromString(uuid)).map(mapper::mapToAusschreibung)
                    .doOnNext(ausschreibung -> {
                        ui.access(() -> {
                            formData = ausschreibung;
                            System.out.println("Received Ausschreibung: " + ausschreibung);
                            pageTitle.setText("Edit Ausschreibung: " + ausschreibung.getTitle());
                            content.removeAll();
                            content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));
                            // load the first step form
                            tabs.setSelectedIndex(0);
                        });
                    }).switchIfEmpty(Mono.fromRunnable(() -> {
                        ui.access(() -> {
                            formData = new Ausschreibung();
                            formData.setStatus("Active");
                            formData.setDate(LocalDateTime.now()); // TODO: set to today for now, potentially to be
                                                                   // changed
                                                                   // to another date
                            pageTitle.setText("Create New Ausschreibung");
                            content.removeAll();
                            content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));
                            // load the first step form
                            tabs.setSelectedIndex(0);
                        });
                    })).subscribe();

        } else {
            formData = new Ausschreibung();
            formData.setStatus("Active");
            formData.setDate(LocalDateTime.now()); // TODO: set to today for now,
            pageTitle.setText("Create New Ausschreibung");
            content.removeAll();
            content.add(new AusschreibungBasicInfoForm(formData, ausschreibungService, tabs));
            // load the first step form
            tabs.setSelectedIndex(0);
        }
    }
}
