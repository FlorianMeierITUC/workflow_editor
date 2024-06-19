package de.ketobi.vaadinspringdemo.main.ui;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;

@Route(value = "welcome", layout = MainLayout.class)
@PageTitle("Welcome")
public class WelcomeView extends VerticalLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
    public WelcomeView() {
        add(new H3("Welcome to the Vaadin Spring Demo Application - Workflow Ticket System!"));
        add(new H4("Please note that this is a demo application in the early stages of development."));
        add(new H4("Please feel free to explore the application and provide feedback."));
        add(new H4("Not only errors and bugs might occur but you may also encounter unfinished features."));
        add(new H4("Thank you for your interest in this project!"));

    }
}
