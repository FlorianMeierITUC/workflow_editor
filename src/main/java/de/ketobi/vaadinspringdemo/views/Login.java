package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
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
                MainLayout mainLayout = (MainLayout) getUI().orElseThrow().getChildren().findFirst().orElseThrow();
                mainLayout.updateNavigation();
                loginForm.getUI().ifPresent(ui -> ui.navigate("todos"));
            } else {
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }
}