package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import java.time.LocalDate;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungBasicInfoForm;

@Route("ausschreibungDetail")
public class AusschreibungDetailView extends VerticalLayout implements HasUrlParameter<String> {

    private final Tabs tabs;
    private final Div content;
    private final AusschreibungService ausschreibungService;
    private Ausschreibung ausschreibung;
    private H1 pageTitle;

    public AusschreibungDetailView(AusschreibungService ausschreibungService) {
        this.ausschreibungService = ausschreibungService;

        pageTitle = new H1();
        add(pageTitle);

        Tab projektUbersicht = new Tab("Projektübersicht");
        Tab KIChat = new Tab("KI Chat");
        Tab dokManagement = new Tab("Dokumenten Management");
        Tab questions = new Tab("Participant Questions");
        Tab angebot = new Tab("Angebot erstellen und managen");

        tabs = new Tabs(projektUbersicht, KIChat, dokManagement, questions, angebot);
        tabs.setWidthFull();

        content = new Div();
        content.setWidthFull();
        content.setSizeFull();

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            Tab selected = event.getSelectedTab();

            if (selected.equals(projektUbersicht)) {
                content.add(new AusschreibungProjektUbersicht(ausschreibung));
            } else if (selected.equals(KIChat)) {
                content.add(new AusschreibungKIChat(ausschreibung));
            } else if (selected.equals(dokManagement)) {
                content.add(new AusschreibungDokManagement(ausschreibung));
            } else if (selected.equals(questions)) {
                content.add(new AusschreibungQuestions(ausschreibung));
            } else if (selected.equals(angebot)) {
                content.add(new AusschreibungAngebotErstellen(ausschreibung));
            }
        });

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        add(tabs, content);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        if (id == null) {
            UI.getCurrent().navigate("ausschreibung/create");
            return;
        }
        ausschreibungService.findById(id).ifPresentOrElse(
            loaded -> {
                ausschreibung = loaded;
                pageTitle.setText("Ausschreibung " + loaded.getTitle());
            },
            () -> {
                UI.getCurrent().navigate("ausschreibung/create");
            }
        );
        tabs.setSelectedIndex(0);
        content.removeAll();
        content.add(new AusschreibungProjektUbersicht(ausschreibung));
    }
}
