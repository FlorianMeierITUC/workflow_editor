package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login page")
public class Login extends VerticalLayout {
    public Login(){
        add(new H3("Login"));
        add(new LoginForm());
    }
}
