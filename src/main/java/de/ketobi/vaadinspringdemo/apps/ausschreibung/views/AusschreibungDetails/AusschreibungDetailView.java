package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import java.time.LocalDateTime;
import java.util.UUID;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
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

import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import reactor.core.publisher.Mono;

@Route("ausschreibungDetail")
public class AusschreibungDetailView extends VerticalLayout implements HasUrlParameter<String> {

    private final Tabs tabs;
    private final Div content;
    private final AusschreibungService ausschreibungService;
    private final Mapper mapper;
    private Ausschreibung ausschreibung;
    private final H1 pageTitle;

    private final Tab projektUbersicht;
    private final Tab KIChat;
    private final Tab dokManagement;
    private final Tab questions;
    private final Tab angebot;

    public AusschreibungDetailView(AusschreibungService ausschreibungService, Mapper mapper) {
        this.ausschreibungService = ausschreibungService;
        this.mapper = mapper;

        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        pageTitle = new H1("Lade Ausschreibung...");
        add(pageTitle);

        // Tabs initialization
        projektUbersicht = new Tab("Projektübersicht");
        KIChat = new Tab("KI Chat");
        dokManagement = new Tab("Dokumenten Management");
        questions = new Tab("Participant Questions");
        angebot = new Tab("Angebot erstellen und managen");

        tabs = new Tabs(projektUbersicht, KIChat, dokManagement, questions, angebot);
        tabs.setWidthFull();

        content = new Div();
        content.setWidthFull();
        content.setSizeFull();
        content.add(new Span("Lade Inhalt..."));

        // Tab listener
        tabs.addSelectedChangeListener(event -> {
            if (ausschreibung == null) {
                content.removeAll();
                content.add(new Span("Ausschreibung wird noch geladen..."));
                return;
            }
            showTabContent(event.getSelectedTab());
        });

        add(tabs, content);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String uuid) {
        if (uuid != null) {
            System.out.println("Received UUID: " + uuid);
            UI ui = UI.getCurrent();

            ausschreibungService.getProjectDetails(UUID.fromString(uuid))
                .map(mapper::mapToAusschreibung)
                .doOnNext(a -> {
                    ausschreibung = a;

                    if (ausschreibung == null) {
                        System.err.println("Ausschreibung mapping returned null!");
                        ui.access(() -> {
                            pageTitle.setText("Fehler: Ausschreibung konnte nicht geladen werden.");
                            content.removeAll();
                            content.add(new Span("Fehler: Keine Daten gefunden."));
                        });
                        return;
                    }

                    System.out.println("Ausschreibung geladen: " + ausschreibung.getTitle());

                    ui.access(() -> {
                        pageTitle.setText("Ausschreibung: " + ausschreibung.getTitle());
                        content.removeAll();
                        showTabContent(tabs.getSelectedTab());
                    });
                })
                .switchIfEmpty(Mono.fromRunnable(() -> {
                    ausschreibung = new Ausschreibung();
                    ausschreibung.setStatus("Active");
                    ausschreibung.setDate(LocalDateTime.now());
                    System.out.println("Keine Ausschreibung gefunden — neues Objekt erstellt.");

                    ui.access(() -> {
                        pageTitle.setText("Neue Ausschreibung erstellen");
                        content.removeAll();
                        showTabContent(tabs.getSelectedTab());
                    });
                }))
                .doOnError(err -> {
                    err.printStackTrace();
                    ui.access(() -> {
                        pageTitle.setText("Fehler beim Laden");
                        content.removeAll();
                        content.add(new Span("Beim Laden der Ausschreibung ist ein Fehler aufgetreten."));
                    });
                })
                .subscribe();
        } else {
            System.out.println("Keine UUID übergeben — keine Ausschreibung geladen.");
            pageTitle.setText("Keine Ausschreibung angegeben");
            content.removeAll();
            content.add(new Span("Bitte eine gültige Ausschreibungs-ID angeben."));
        }
    }

    private void showTabContent(Tab selected) {
        content.removeAll();
        System.out.println("Tab ausgewählt: " + selected.getLabel());

        if (ausschreibung == null) {
            System.err.println("Fehler: Ausschreibung ist noch null beim Rendern des Tabs!");
            content.add(new Span("Ausschreibung wird noch geladen..."));
            return;
        }

        if (selected.equals(projektUbersicht)) {
            content.add(new AusschreibungProjektUbersicht(ausschreibung, ausschreibungService));
        } else if (selected.equals(KIChat)) {
            content.add(new AusschreibungKIChat(ausschreibung, ausschreibungService));
        } else if (selected.equals(dokManagement)) {
            content.add(new AusschreibungDokManagement(ausschreibung, ausschreibungService));
        } else if (selected.equals(questions)) {
            content.add(new AusschreibungQuestions(ausschreibung));
        } else if (selected.equals(angebot)) {
            content.add(new AusschreibungAngebotErstellen(ausschreibung));
        } else {
            content.add(new Span("Unbekannter Tab ausgewählt."));
        }
    }
}
