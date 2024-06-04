package de.ketobi.vaadinspringdemo.apps.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Route(value = "createdemoobject", layout = MainLayout.class)
@PageTitle("Create a demo object")
public class CreateDemoObject extends VerticalLayout implements BeforeEnterObserver {
    private final DemoObjectService demoObjectService;
    private final WorkflowService workflowService;
    private final WorkflowEntityService workflowEntityService;
    private final TextField nameField;
    private final TextArea descriptionField;

    public CreateDemoObject(DemoObjectService demoObjectService, WorkflowService workflowService, WorkflowEntityService workflowEntityService){
        this.demoObjectService = demoObjectService;
        this.workflowService = workflowService;
        this.workflowEntityService = workflowEntityService;
        add(new H3("Create Demo Object"));
        add(new H4("Create a new demo object to show the capabilities of the workflow engine."));
        nameField = new TextField("Name");
        descriptionField = new TextArea("Description");
        add(nameField, descriptionField);
        add(new Button("Save", e -> {
            DemoObject demoObject = DemoObject.builder()
                    .id(ObjectId.get())
                    .name(nameField.getValue())
                    .description(descriptionField.getValue())
                    .createdAt(LocalDateTime.now())
                    .createdBy(UserService.getCurrentUser().getId())
                    .build();
            demoObjectService.save(demoObject);
            workflowEntityService.startWorkflow(demoObject);
            nameField.clear();
            descriptionField.clear();
        }));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
}