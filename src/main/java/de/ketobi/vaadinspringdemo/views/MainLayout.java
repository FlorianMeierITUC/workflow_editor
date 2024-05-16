package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {
    private Div content;
    private Navigation navigation;

    public MainLayout() {
        navigation = new Navigation();
        content = new Div();
        content.setId("content");
        add(new H3("Main Layout"));
        add(navigation, content);
    }

    public void updateNavigation(){
        remove(navigation, content);
        navigation = new Navigation();
        add(navigation, content);
    }
}