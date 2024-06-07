package de.ketobi.vaadinspringdemo.main.ui.header;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinService;
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
        Button userButton = new Button(new Icon(VaadinIcon.USER));
        //<theme-editor-local-classname>
        userButton.addClassName("header-layout-button-1");
        userButton.addThemeVariants(ButtonVariant.LUMO_ICON);

        ContextMenu userMenu = new ContextMenu(userButton);
        userMenu.setOpenOnClick(true);
        userMenu.addItem(new Text(UserService.getCurrentUser().getName())).setEnabled(false);
        userMenu.add(new Hr());

        userMenu.addItem("Logout", e -> {
            VaadinSession.getCurrent().setAttribute("user", null);
            VaadinService.getCurrentRequest().getWrappedSession().invalidate();
            UI.getCurrent().navigate("");
        });

        userMenu.addItem("Settings", e -> {
            UI.getCurrent().navigate("usersettings");
        });
        add(userButton);
    }
}
