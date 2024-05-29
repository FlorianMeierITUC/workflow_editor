package de.ketobi.vaadinspringdemo.main.navigation.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import de.ketobi.vaadinspringdemo.main.navigation.NavigationLayout;
import de.ketobi.vaadinspringdemo.main.navigation.services.NavigationService;
import org.bson.types.ObjectId;

public class AddTargetButton extends Button {
    public AddTargetButton(ObjectId folderId, NavigationService navigationService, NavigationLayout navigationLayout) {
        super("+ Target");
        addClickListener(event -> {
            Dialog addTargetDialog = new Dialog();
            VerticalLayout dialogLayout = new VerticalLayout();

            dialogLayout.add("Add Target");
            TextField label = new TextField("Label *");
            NumberField index = new NumberField("Index *");
            TextField view = new TextField("View");
            view.setValue("/");
            dialogLayout.add(label, index, view);

            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (e2) -> addTargetDialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            addTargetDialog.getHeader().add(closeButton);
            addTargetDialog.add(dialogLayout);
            addTargetDialog.getFooter().add(new Button("Save", (e) -> {
                if (label.getValue().isEmpty() || index.getValue()==null){
                    Notification notification = Notification
                            .show("Label and Index must be provided!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    navigationService.saveNewTarget(folderId, label.getValue(), view.getValue(), index.getValue().intValue());
                    addTargetDialog.close();
                    navigationLayout.refresh();
                }
            }));
            addTargetDialog.open();
        });
    }
}
