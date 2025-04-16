package de.ketobi.vaadinspringdemo.apps.auschreibung.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route("auschreibung/create")
@PageTitle("Create Tender")
public class AuschreibungCreateView extends VerticalLayout {

    private int step = 1;

    private final VerticalLayout stepLayout = new VerticalLayout();
    private final Button back = new Button("Back");
    private final Button next = new Button("Next");

    public AuschreibungCreateView() {
        updateStep();

        back.addClickListener(e -> {
            if (step > 1) {
                step--;
                updateStep();
            }
        });

        next.addClickListener(e -> {
            if (step < 3) {
                step++;
                updateStep();
            } else {
                Notification.show("Tender submitted!");
                getUI().ifPresent(ui -> ui.navigate("auschreibung"));
            }
        });

        HorizontalLayout buttonBar = new HorizontalLayout(back, next);
        add(stepLayout, buttonBar);
    }

    private void updateStep() {
        stepLayout.removeAll();

        switch (step) {
            case 1 -> stepLayout.add(new H2("Step 1: Tender Info"));
            case 2 -> stepLayout.add(new H2("Step 2: Documents"));
            case 3 -> stepLayout.add(new H2("Step 3: Review & Submit"));
        }

        back.setEnabled(step > 1);
        next.setText(step < 3 ? "Next" : "Submit");
    }
}
