package de.ketobi.vaadinspringdemo.apps.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;

@Route(value = "664e4afd9228b34647a78398", layout = MainLayout.class)
@PageTitle("User Decision")
public class UserDecision extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final DemoObjectService demoService;
    private DemoObject demoObject;
    private TextField message;

    public UserDecision(DemoObjectService demoService, WorkflowItemService workflowItemService){
        this.demoService = demoService;
        add(new H3("User Decision"));
        add(new H4("Please make a decision."));
        message = new TextField("Message");

        Button successButton = new Button("Success");
        successButton.addClickListener(e -> {
            workflowItemService.nextNode(demoObject, true, "Success. Message: " + message.getValue());
            successButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        Button failureButton = new Button("Failure");
        failureButton.addClickListener(e -> {
            workflowItemService.nextNode(demoObject, false, "Failure. Message: " + message.getValue());
            failureButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        add(new Paragraph("Please add a message. This message will be visible to the next user in the workflow."));
        add(message);
        add(successButton, failureButton);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String demoObjectId) {
        this.demoObject = demoService.getById(demoObjectId);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
}