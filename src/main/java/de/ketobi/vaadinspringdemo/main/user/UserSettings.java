package de.ketobi.vaadinspringdemo.main.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;

@Route(value = "usersettings", layout = MainLayout.class)
@PageTitle("Configure your account")
public class UserSettings extends VerticalLayout implements BeforeEnterObserver {
    private final UserService userService;
    private User user;
    private final Select<User> substituteSelect = new Select<>();

    public UserSettings(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            this.user = UserService.getCurrentUser();
            add(new H3("User settings"));
            add(new H4("Name: " + user.getName()));
            add(new H4("Email: " + user.getEmail()));
            add(new Hr());
            add(new H4("A substitute user can be selected here who can see and process your workflow tickets if you are not available."));
            add(substituteSelect);
            add(new SaveSubstituteButton());
            substituteSelect.setLabel("Select a substitute user");
            substituteSelect.setItems(userService.getAllUsersExceptTheCurrentUser());
            substituteSelect.setEmptySelectionAllowed(true);
            substituteSelect.setEmptySelectionCaption("No substitute");
            if(user.getSubstituteUserId() != null) {
                substituteSelect.setValue(userService.getUserById(user.getSubstituteUserId()));
            }
        }
    }

    private class SaveSubstituteButton extends Button {
        public SaveSubstituteButton() {
            super("Save substitute user");
            addClickListener(e -> {
                if(substituteSelect.getValue()==null){
                    user.setSubstituteUserId(null);
                } else {
                    user.setSubstituteUserId(substituteSelect.getValue().getId());
                }
                userService.save(user);
                Notification notification = Notification
                        .show("Substitute user saved!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                substituteSelect.clear();
            });
        }
    }
}
