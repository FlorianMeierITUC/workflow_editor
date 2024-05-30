package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.main.header.HeaderLayout;
import de.ketobi.vaadinspringdemo.main.navigation.NavigationLayout;
import de.ketobi.vaadinspringdemo.services.UserService;

@SpringComponent
@UIScope
public class MainLayout extends AppLayout {
    private NavigationLayout navigation;
    private HeaderLayout header;

    public MainLayout(NavigationLayout navigationLayout, HeaderLayout headerLayout){
        this.navigation = navigationLayout;
        this.header = headerLayout;
        DrawerToggle toggle = new DrawerToggle();
        addToNavbar(true, toggle, headerLayout);
        addToDrawer(navigation);
    }
}