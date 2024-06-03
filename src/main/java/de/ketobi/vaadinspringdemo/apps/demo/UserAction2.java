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
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import org.bson.types.ObjectId;

@Route(value = "6659d0582f3f524e48b12090", layout = MainLayout.class)
@PageTitle("User Action 2")
public class UserAction2 extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private final DemoObjectService demoService;
    private final WorkflowTicketService workflowTicketService;
    private WorkflowTicket workflowTicket;
    private DemoObject demoObject;
    private TextField message;

    public UserAction2(DemoObjectService demoService, WorkflowItemService workflowItemService, WorkflowTicketService workflowTicketService){
        this.demoService = demoService;
        this.workflowTicketService = workflowTicketService;
        add(new H3("User Action 2"));
        add(new H4("Please add a message and proceed to the next node."));
        message = new TextField("Message");

        Button nextNodeButton = new Button("Next Node");
        nextNodeButton.addClickListener(e -> {
            workflowItemService.nextNode(workflowTicket, null, "Message: " + message.getValue());
            nextNodeButton.getUI().ifPresent(ui -> ui.navigate("workflowtickets"));
        });

        add(new Paragraph("Please add a message. This message will be visible to the next user in the workflow."));
        add(message);
        add(nextNodeButton);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String ticketId) {
        this.workflowTicket = workflowTicketService.getWorkflowTicket(new ObjectId(ticketId));
        this.demoObject = demoService.getById(workflowTicket.getWorkflowEntityId());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }
}