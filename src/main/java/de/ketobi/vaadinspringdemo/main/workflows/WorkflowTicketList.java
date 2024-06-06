package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowTicketHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicketHistory;
import de.ketobi.vaadinspringdemo.main.workflows.services.*;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route(value = "workflowtickets", layout = MainLayout.class)
@PageTitle("My Workflow Tickets")
public class WorkflowTicketList  extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowEntityService workflowEntityService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private final UserService userService;
    private final WorkflowTicketService workflowTicketService;
    private final WorkflowTicketHistoryService workflowTicketHistoryService;

    private Grid<WorkflowTicket> workflowTicketsGrid;
    private List<WorkflowTicket> workflowTickets = new ArrayList<>();
    private GridListDataView<WorkflowTicket> workflowTicketsView;

    public WorkflowTicketList(
            WorkflowEntityService workflowEntityService,
            WorkflowService workflowService,
            WorkflowNodeService workflowNodeService,
            UserService userService,
            WorkflowTicketService workflowTicketService,
            WorkflowTicketHistoryService workflowTicketHistoryService){
        this.workflowEntityService = workflowEntityService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.userService = userService;
        this.workflowTicketHistoryService = workflowTicketHistoryService;

        workflowTicketsGrid = createMyWorkflowTicketsGrid();
        workflowTicketsView = workflowTicketsGrid.setItems(workflowTickets);
        add(new H3("Workflow tickets"));
        add(new H4("The workflow tickets assigned to me."));
        add(workflowTicketsGrid);
        this.workflowTicketService = workflowTicketService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            workflowTickets = workflowEntityService.getAllWorkflowTicketsAssignedToTheCurrentUser();
            workflowTicketsView = workflowTicketsGrid.setItems(workflowTickets);
        }
    }

    private Grid<WorkflowTicket> createMyWorkflowTicketsGrid() {
        Grid<WorkflowTicket> workflowTicketsGrid = new Grid<>(WorkflowTicket.class, false);
        workflowTicketsGrid.addColumn(wfTicket -> workflowService.getById(wfTicket.getWorkflowId()).getName()).setHeader("Workflow").setAutoWidth(true);
        workflowTicketsGrid.addColumn(wfTicket -> workflowNodeService.getById(wfTicket.getCurrentNodeId()).getTitle()).setHeader("Current Node").setAutoWidth(true);
        workflowTicketsGrid.addColumn(wfTicket -> workflowTicketService.getWorkflowEntity(wfTicket).getName()).setHeader("Entity name").setAutoWidth(true);
        workflowTicketsGrid.addColumn(wfTicket -> workflowTicketService.getOpenSinceDays(wfTicket)).setHeader("Open since # days").setAutoWidth(true);
        workflowTicketsGrid.addComponentColumn(wfTicket -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                WorkflowNode node = workflowNodeService.getById(wfTicket.getCurrentNodeId());
                getUI().ifPresent(ui -> ui.navigate("/"+node.getId()+"/"+wfTicket.getId()));
            });
            Button historyButton = new Button("History");
            historyButton.addClickListener(e -> {
                WorkflowTicketHistoryDialog dialog = new WorkflowTicketHistoryDialog(workflowEntityService.getWorkflowEntityHistory(workflowTicketService.getWorkflowEntity(wfTicket)), workflowEntityService);
                dialog.open();
            });
            Button workflowViewButton = new Button("Workflow");
            workflowViewButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> nodes = workflowNodeService.getAll(workflowService.getById(wfTicket.getWorkflowId()).getId());
                dialog.add(new WorkflowView(nodes, List.of(wfTicket.getCurrentNodeId())));
                dialog.open();
            });
            Button forwardButton = new Button("Forward");
            forwardButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                Button closeButton = new Button(new Icon("lumo", "cross"),
                        (e2) -> dialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dialog.getHeader().add(closeButton);
                Select<User> responsible = new Select<>();
                responsible.setLabel("Responsible");
                responsible.setItems(userService.getAllUsersExceptTheCurrentUser());
                responsible.setItemLabelGenerator(User::getName);
                TextArea message = new TextArea();
                Button saveButton = new Button("Save", e3 -> {
                    wfTicket.setCurrentResponsibleId(responsible.getValue().getId());
                    workflowTicketService.save(wfTicket);
                    User user = UserService.getCurrentUser();
                    Workflow wf = workflowService.getById(wfTicket.getWorkflowId());
                    WorkflowNode node = workflowNodeService.getById(wfTicket.getCurrentNodeId());
                    WorkflowTicketHistory history = WorkflowTicketHistory.builder()
                            .ticketId(wfTicket.getId())
                            .workflowName(wf.getName())
                            .nodeTitle(node.getTitle())
                            .entityId(workflowTicketService.getWorkflowEntity(wfTicket).getId())
                            .message("Item forwarded: "+user.getName()+" -> "+responsible.getValue().getName()+". Message: "+message.getValue())
                            .responsibleUser(user.getName())
                            .createdAt(LocalDateTime.now())
                            .build();
                    workflowEntityService.writeWorkflowHistoryEntry(history);
                    workflowTicketsView.removeItem(wfTicket);
                    dialog.close();
                });
                dialog.add(new Span("Forward item to:"));
                dialog.add(responsible);
                dialog.add(new Span("Message:"));
                dialog.add(message);
                dialog.getFooter().add(saveButton);
                dialog.open();
            });
            buttonDiv.add(editButton);
            buttonDiv.add(forwardButton);
            buttonDiv.add(workflowViewButton);
            buttonDiv.add(historyButton);
            return buttonDiv;
        }).setHeader("Actions").setAutoWidth(true);
        workflowTicketsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        workflowTicketsGrid.setAllRowsVisible(true);
        return workflowTicketsGrid;
    }

}
