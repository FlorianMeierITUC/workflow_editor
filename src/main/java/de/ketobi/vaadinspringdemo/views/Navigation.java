package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.services.UserService;

public class Navigation extends HorizontalLayout {
    public Navigation() {
        if (UserService.getCurrentUser() != null) {
            add(createButton("Login", Login.class));
            add(createButton("Todos", TodoList.class));
            add(createButton("Workflow List", WorkflowList.class));
            add(createButton("Create Order", CreateOrder.class));
            add(createButton("Users", UserView.class));
            add(new Paragraph(UserService.getCurrentUser().getName()));
        }
    }

    private Button createButton(String text, Class<? extends Component> navigationTarget){
        Button button = new Button(text);
        button.addClickListener(event -> button.getUI().ifPresent(ui -> ui.navigate(navigationTarget)));
        return button;
    }
}