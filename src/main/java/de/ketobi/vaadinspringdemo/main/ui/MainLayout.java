package de.ketobi.vaadinspringdemo.main.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.main.ui.header.HeaderLayout;
import de.ketobi.vaadinspringdemo.main.ui.navigation.NavigationLayout;

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