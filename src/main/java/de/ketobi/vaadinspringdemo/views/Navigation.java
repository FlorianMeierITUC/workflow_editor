package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;

public class Navigation extends HorizontalLayout {
    public Navigation() {
        add(createButton("Login", Login.class));
        if (VaadinSession.getCurrent().getAttribute("user") != null) {
            add(createButton("Todos", TodoList.class));
            add(createButton("Workflow List", WorkflowList.class));
            add(createButton("Create Order", CreateOrder.class));
            add(createButton("Users", UserView.class));
        }
    }

    private Button createButton(String text, Class<? extends Component> navigationTarget){
        Button button = new Button(text);
        button.addClickListener(event -> button.getUI().ifPresent(ui -> ui.navigate(navigationTarget)));
        return button;
    }
}