package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.apps.auschreibung.components.GridArchiv;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "auschreibung", layout = MainLayout.class)
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
            e -> getUI().ifPresent(ui -> ui.navigate("auschreibung/create"))
        );
        createButton.addClassName("neue-auschreibung");
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
