package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;

public class AusschreibungQuestions extends VerticalLayout {

    public AusschreibungQuestions(Ausschreibung ausschreibung) {
        add(new H1("Inhaltsverzeichnis generieren"));
        add(new H1("Strukturen und Inhalte vergleichen"));
        add(new H1("Vorschläge von der KI"));
        add(new H1("Kontrolle auf Ausdrucksweise, Rechtschreibung, Copy-and-Paste-Fehler und faktische Fehler"));

        //TODO: potentially add the button abbrechen und speichern if logical to be put. Have to get more details about this view.
    }
}
