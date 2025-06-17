package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AusschreibungDokManagement extends VerticalLayout {


    public AusschreibungDokManagement(Ausschreibung ausschreibung) {
        
        add(new H1("Dokumenten Management"));
        
        //TODO: get more details about this view and add the necessary components.
    }
}