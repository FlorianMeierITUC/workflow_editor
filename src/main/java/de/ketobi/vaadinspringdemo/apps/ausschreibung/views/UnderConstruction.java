package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;


@Route(value ="under-construction", layout = MainLayout.class)
public class UnderConstruction extends VerticalLayout {

    public UnderConstruction() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        H1 message = new H1("Diese Seite befindet sich im Aufbau.");
        message.getStyle().set("color", "white");
        message.getStyle().set("text-align", "center");
        message.getStyle().set("font-size", "2rem");
        message.getStyle().set("font-weight", "bold");

         Image logo = new Image("/images/under-construction.png", "Robot Under Construction");
        logo.getStyle().set("margin", "2rem auto").set("display", "block").set("height", "22rem").set("width", "20rem");
        add(logo);

        add(message);
    }
   
}