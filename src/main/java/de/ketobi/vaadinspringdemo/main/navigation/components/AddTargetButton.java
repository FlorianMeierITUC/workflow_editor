package de.ketobi.vaadinspringdemo.main.navigation.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AddTargetButton extends Button {
    public AddTargetButton() {
        super("+ Target");
        addClickListener(event -> {
            Dialog addTargetDialog = new Dialog();
            VerticalLayout dialogLayout = new VerticalLayout();
            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (e2) -> addTargetDialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addTargetDialog.getHeader().add(closeButton);
            addTargetDialog.add(dialogLayout);
            addTargetDialog.open();
        });
    }
}
