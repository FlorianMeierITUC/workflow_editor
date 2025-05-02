package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import de.ketobi.vaadinspringdemo.main.ui.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route(value = "auschreibung", layout = MainLayout.class)
@PageTitle("Auschreibungrpojekte APP")
public class AuschreibungView extends VerticalLayout {

    public AuschreibungView() {

        //add(new Button("Hello from Auschreibung Page!"));
        add(new H1("Neues Auschreinungs-Projekt anlegen"));
        Button createButton = new Button("+ Klick hier, um ein neues Auschreibungprojekt anzulegen",
            e -> getUI().ifPresent(ui -> ui.navigate("auschreibung/create"))
        );
        createButton.getElement().setAttribute("theme", "neue-auschreibung");
        add(createButton);

        add(new H1("Archiv"));
        add(new H1("Favorisierte Einträge"));

        


        add(new H1("Alle Archiv Einträge"));

    
    }
}
