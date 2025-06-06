package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;

public class AusschreibungAngebotErstellen extends VerticalLayout   {

    public AusschreibungAngebotErstellen(Ausschreibung ausschreibung) {

    add(new H1("Bestehenden FAQ-Liste"));
    add(new H1("Sanity Check"));
    add(new H1("Eigene Fragen hinzufügen"));
    add(new H1("KI-gestützte Vorschläge für Fragen"));

    //TODO: potentially add the button abbrechen und speichern if logical to be put. Have to get more details about this view.

    }

}