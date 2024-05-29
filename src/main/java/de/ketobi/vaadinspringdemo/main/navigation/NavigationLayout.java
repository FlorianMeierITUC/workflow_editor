package de.ketobi.vaadinspringdemo.main.navigation;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.main.navigation.components.AddFolderButton;
import de.ketobi.vaadinspringdemo.main.navigation.components.AddTargetButton;
import de.ketobi.vaadinspringdemo.main.navigation.services.NavigationService;
import de.ketobi.vaadinspringdemo.services.UserService;
import de.ketobi.vaadinspringdemo.views.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NavigationLayout extends VerticalLayout {
    private final NavigationService navigationService;
    private final Div navigationDiv = new Div();

    @Autowired
    public NavigationLayout(NavigationService navigationService) {
        this.navigationService = navigationService;
        setSizeUndefined();
        if (UserService.getCurrentUser() != null) {
            refresh();
        }
    }

    private void addLoginNav(){
        SideNav loginNav = new SideNav();
        loginNav.setLabel("Login");
        loginNav.setCollapsible(true);
        loginNav.addItem(new SideNavItem("Login", Login.class));
        navigationDiv.add(loginNav);
    }

    private void addTodosNav(){
        SideNav todosNav = new SideNav();
        todosNav.setLabel("Todos");
        todosNav.setCollapsible(true);
        todosNav.addItem(new SideNavItem("Todos", TodoList.class));
        navigationDiv.add(todosNav);
    }

    private void addOrdersNav(){
        SideNav ordersNav = new SideNav();
        ordersNav.setLabel("Orders");
        ordersNav.setCollapsible(true);
        ordersNav.addItem(new SideNavItem("Create Order", CreateOrder.class));
        navigationDiv.add(ordersNav);
    }

    private void addAdminNav(){
        SideNav adminNav = new SideNav();
        adminNav.setLabel("Admin");
        adminNav.setCollapsible(true);
        adminNav.addItem(new SideNavItem("Users", UserView.class));
        adminNav.addItem(new SideNavItem("Workflow List", WorkflowList.class));
        navigationDiv.add(adminNav);
    }

    public void refresh(){
        removeAll();
        addLoginNav();
        addAdminNav();
        addTodosNav();
        addOrdersNav();
        Scroller scroller = new Scroller(navigationDiv);
        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        add(scroller);
        add(new Hr());
        add(new AddFolderButton(navigationService));
        add(new AddTargetButton());
    }
}