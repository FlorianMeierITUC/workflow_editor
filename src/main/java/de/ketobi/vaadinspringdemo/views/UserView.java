package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.repositories.UserRepository;
import de.ketobi.vaadinspringdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;

@Route(value = "user", layout = MainLayout.class)
@PageTitle("Create and manage users")
public class UserView extends VerticalLayout implements BeforeEnterObserver {
    private final UserRepository userRepository;
    private TextField name = new TextField("Name *");
    private TextField email = new TextField("Email");
    private TextField password = new TextField("Password *");
    private GridListDataView<User> userView;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            //TODO removed for testing
            //event.forwardTo(Login.class);
        }
    }

    @Autowired
    public UserView(UserRepository userRepository){
        this.userRepository = userRepository;
        ArrayList<User> user = new ArrayList<>(userRepository.findAll());
        Grid<User> userGrid = new Grid<>(User.class, false);
        userGrid.addColumn(User::getId).setHeader("ID").setAutoWidth(true);
        userGrid.addColumn(User::getName).setHeader("Name").setAutoWidth(true);
        userGrid.addColumn(User::getEmail).setHeader("Email").setAutoWidth(true);
        userGrid.addComponentColumn(selectedUser -> {
                    Button deleteButton = new Button("Delete");
                    deleteButton.addClickListener(e -> {
                        userRepository.delete(selectedUser);
                        Notification notification = Notification
                                .show("User deleted!");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        userView.removeItem(selectedUser);
                    });
                    return deleteButton;
                });
        userGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);

        userView = userGrid.setItems(user);

        add(new H3("Users"));
        add(new Paragraph("New User:"));
        add(name);
        add(email);
        add(password);
        add(new SaveButton());
        userView.addItemCountChangeListener(e ->
                Notification.show(e.getItemCount() + " items available"));

        Span itemCountSpan = new Span("Total Item Count: " + userView.getItemCount());
        add(itemCountSpan);
        add(userGrid);
    }

    private class SaveButton extends Button {
        SaveButton(){
            setText("+ Add");
            addSingleClickListener(clickEvent -> {
                User user = new User();
                user.setName(name.getValue());
                if(null == name.getValue() || name.getValue().isEmpty() || name.getValue().isBlank()){
                    Notification notification = Notification
                            .show("Please provide a name for the user!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                    return;
                }
                user.setEmail(email.getValue());
                if(null == email.getValue() || email.getValue().isEmpty() || email.getValue().isBlank()){
                    Notification notification = Notification
                            .show("Please provide an email for the user!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                    return;
                }
                user.setPassword(password.getValue());
                if(null == password.getValue() || password.getValue().isEmpty() || password.getValue().isBlank()){
                    Notification notification = Notification
                            .show("Please provide a password for the user!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                    return;
                }

                try {
                    userRepository.save(user);
                    Notification notification = Notification
                            .show("User created!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    userView.addItem(user);
                } catch (DuplicateKeyException ex){
                    Notification notification = Notification
                            .show("Entry with this name already present! Choose a different name!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
        }

    }
}