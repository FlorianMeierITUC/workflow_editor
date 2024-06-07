package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicketHistory;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SpringComponent
public class WorkflowTicketsGrid extends Grid<WorkflowTicket> {
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowTicketService workflowTicketService;
    private final WorkflowEntityService workflowEntityService;
    private final UserService userService;

    private GridListDataView<WorkflowTicket> workflowTicketsView;

    @Autowired
    public WorkflowTicketsGrid(List<WorkflowTicket> workflowTickets,
                               WorkflowService workflowService,
                               WorkflowNodeService workflowNodeService,
                               WorkflowTicketService workflowTicketService,
                               WorkflowEntityService workflowEntityService,
                               UserService userService) {
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.workflowTicketService = workflowTicketService;
        this.workflowEntityService = workflowEntityService;
        this.userService = userService;
        this.workflowTicketsView = setItems(workflowTickets);
        addColumn(wfTicket -> workflowService.getById(wfTicket.getWorkflowId()).getName()).setHeader("Workflow").setAutoWidth(true);
        addColumn(wfTicket -> workflowNodeService.getById(wfTicket.getCurrentNodeId()).getTitle()).setHeader("Current Node").setAutoWidth(true);
        addColumn(wfTicket -> workflowTicketService.getWorkflowEntity(wfTicket).getName()).setHeader("Entity name").setAutoWidth(true);
        addColumn(wfTicket -> workflowTicketService.getOpenSinceDays(wfTicket)).setHeader("Open since # days").setAutoWidth(true);
        addComponentColumn(wfTicket -> {
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
        addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        setAllRowsVisible(true);
    }
}
