package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowTicketsGrid;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;

import java.util.List;

@Route(value = "workflowticketsubstitutions", layout = MainLayout.class)
@PageTitle("Workflow Ticket Substitutions")
public class WorkflowTicketSubstitutionsList  extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowEntityService workflowEntityService;
    private final UserService userService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowTicketService workflowTicketService;

    public WorkflowTicketSubstitutionsList(WorkflowEntityService workflowEntityService,
                                           UserService userService,
                                           WorkflowService workflowService,
                                           WorkflowNodeService workflowNodeService,
                                           WorkflowTicketService workflowTicketService){
        this.workflowEntityService = workflowEntityService;
        this.userService = userService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.workflowTicketService = workflowTicketService;

        add(new H3("Workflow tickets substitutions"));
        add(new H4("The workflow tickets assigned to me because i am the substitute for the specified user."));

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            for (User user : userService.getAllSubstitutions(UserService.getCurrentUser())) {
                List<WorkflowTicket> workflowTickets = workflowEntityService.getAllWorkflowTicketsAssignedTo(user);
                Grid<WorkflowTicket> workflowTicketsGrid = new WorkflowTicketsGrid(
                        workflowTickets,
                        workflowService,
                        workflowNodeService,
                        workflowTicketService,
                        workflowEntityService,
                        userService);
                add(new H4("Tickets of: " + user));
                add(workflowTicketsGrid);
                add(new Hr());
            }
        }
    }
}
