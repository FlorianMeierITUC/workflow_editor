package de.ketobi.vaadinspringdemo.main.ui.navigation.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import de.ketobi.vaadinspringdemo.main.ui.navigation.NavigationLayout;
import de.ketobi.vaadinspringdemo.main.ui.navigation.services.NavigationService;

public class AddFolderButton extends Button {
    NavigationService navigationService;

    public AddFolderButton(NavigationService navigationService, NavigationLayout navigationLayout) {
        super("+ Folder");
        this.navigationService = navigationService;
        addClickListener(event -> {
            Dialog addFolderDialog = new Dialog();
            VerticalLayout dialogLayout = new VerticalLayout();

            dialogLayout.add("Add Folder");
            TextField label = new TextField("Label *");
            NumberField index = new NumberField("Index *");
            dialogLayout.add(label);
            dialogLayout.add(index);

            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (e2) -> addFolderDialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            addFolderDialog.getHeader().add(closeButton);
            addFolderDialog.add(dialogLayout);
            addFolderDialog.getFooter().add(new Button("Save", (e) -> {
                if (label.getValue().isEmpty() || index.getValue()==null){
                    Notification notification = Notification
                            .show("Label and Index must be provided!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    navigationService.saveNewFolder(label.getValue(), index.getValue().intValue());
                    addFolderDialog.close();
                    navigationLayout.refresh();
                }
            }));
            addFolderDialog.open();
        });
    }
}
