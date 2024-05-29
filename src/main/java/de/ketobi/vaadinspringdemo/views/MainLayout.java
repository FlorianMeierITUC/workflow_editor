package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.main.navigation.NavigationLayout;
import org.springframework.context.ApplicationContext;

@SpringComponent
@UIScope
public class MainLayout extends HorizontalLayout implements RouterLayout {
    private Div content;
    private NavigationLayout navigation;
    private final ApplicationContext applicationContext;


    public MainLayout(NavigationLayout navigationLayout, ApplicationContext applicationContext){
        this.navigation = navigationLayout;
        this.applicationContext = applicationContext;
        content = new Div();
        content.setId("content");
        add(navigation, content);
    }

    public void updateNavigation(){
        remove(navigation, content);
        navigation.refresh();
        add(navigation, content);
    }
}