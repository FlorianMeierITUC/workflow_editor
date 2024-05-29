package de.ketobi.vaadinspringdemo.main.navigation.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.main.navigation.services.NavigationService;

public class AddFolderButton extends Button {
    NavigationService navigationService;

    public AddFolderButton(NavigationService navigationService) {
        super("+ Folder");
        this.navigationService = navigationService;
        addClickListener(event -> {
            Dialog addFolderDialog = new Dialog();
            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.add("Add Folder");

            dialogLayout.add(new Button("Save", (e) -> {
                // Save folder
                addFolderDialog.close();
            }));
            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (e2) -> addFolderDialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addFolderDialog.getHeader().add(closeButton);
            addFolderDialog.add(dialogLayout);
            addFolderDialog.open();
        });
    }
}
