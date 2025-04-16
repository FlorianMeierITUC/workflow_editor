package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import de.ketobi.vaadinspringdemo.main.ui.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route(value = "auschreibung", layout = MainLayout.class)
@PageTitle("Auschreibungrpojekte APP")
public class AuschreibungView extends VerticalLayout {

    public AuschreibungView() {
        add(new Button("Hello from Auschreibung Page!"));
    }
}
