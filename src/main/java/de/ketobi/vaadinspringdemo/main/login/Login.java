package de.ketobi.vaadinspringdemo.main.login;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "")
@PageTitle("Login page")
public class Login extends VerticalLayout {
    private UserService userService;

    @Autowired
    public Login(UserService userService){
        this.userService = userService;
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(e -> {
            User user = userService.authenticate(e.getUsername(), e.getPassword());
            if (user != null) {
                VaadinSession.getCurrent().setAttribute("user", user);
                loginForm.getUI().ifPresent(ui -> ui.navigate("welcome"));
            } else {
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }
}