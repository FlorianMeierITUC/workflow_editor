package de.ketobi.vaadinspringdemo.main.ui;

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
        add("Welcome to the Vaadin Spring Demo Application!");
    }
}
