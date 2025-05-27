package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.GridArchiv;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "ausschreibung", layout = MainLayout.class)
@CssImport(value = "./themes/my-theme/components/vaadin-button.css", themeFor = "vaadin-button")
@PageTitle("Ausschreibungsprojekte")
public class AusschreibungView extends VerticalLayout {
    private final AusschreibungService ausschreibungService;

    @Autowired
    public AusschreibungView(AusschreibungService ausschreibungService) {
        this.ausschreibungService = ausschreibungService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Create new Tender button
        add(new H1("Neues Ausschreibungsprojekt anlegen"));
        Button createButton = new Button(
            "+ Klicke hier, um ein neues Projekt anzulegen",
            e -> getUI().ifPresent(ui -> ui.navigate("ausschreibung/create"))
        );
        createButton.getElement().setAttribute("theme", "neue-ausschreibung");
        add(createButton);

        add(new H1("Archiv"));
        H2 favorite = new H2("Favorisierte Einträge");
        favorite.getElement().getStyle().set("margin-top", "1em");
        add(favorite);
        
        GridArchiv gridArchivFav = new GridArchiv(ausschreibungService, true);
        add(gridArchivFav);
        
        H2 all = new H2("Alle Einträge");
        all.getElement().getStyle().set("margin-top", "1em");
        add(all);
        
        GridArchiv gridArchivAll = new GridArchiv(ausschreibungService, false);
        gridArchivAll.addFavoriteToggleListener(a -> gridArchivFav.reload());
        add(gridArchivAll);
    
    }
}
