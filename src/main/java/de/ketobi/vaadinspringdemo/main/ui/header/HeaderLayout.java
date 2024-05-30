package de.ketobi.vaadinspringdemo.main.ui.header;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;

@SpringComponent
@UIScope
public class HeaderLayout extends HorizontalLayout implements BeforeEnterObserver {

    public HeaderLayout(){
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.BETWEEN);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (UserService.getCurrentUser() != null) {
            refresh();
        }
    }

    public void refresh(){
        removeAll();
        add(new H3("Vaadin Spring Demo"));
        add(new Span("Welcome, " + UserService.getCurrentUser().getName()));
        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("user", null);
            UI.getCurrent().navigate("");
        });
        add(logoutButton);
    }
}
