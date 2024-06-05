package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowTicketHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowEntity;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicket;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.bson.types.ObjectId;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "workflowentities", layout = MainLayout.class)
@PageTitle("My Workflow Entities")
public class WorkflowEntitiesList extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowEntityService workflowEntityService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowTicketService workflowTicketService;

    private final Grid<WorkflowEntity> myWorkflowEntitiesGrid;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public WorkflowEntitiesList(WorkflowEntityService workflowEntityService, WorkflowService workflowService, WorkflowNodeService workflowNodeService, WorkflowTicketService workflowTicketService) {
        this.workflowEntityService = workflowEntityService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.workflowTicketService = workflowTicketService;
        this.myWorkflowEntitiesGrid = createMyWorkflowEntitiesGrid();

        add(new H3("Workflow entities"));
        add(new H4("The workflow entities i created across all workflows."));
        add(myWorkflowEntitiesGrid);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            List<WorkflowEntity> workflowEntities = workflowEntityService.getAllWorkflowEntitiesCreatedByTheCurrentUser();
            myWorkflowEntitiesGrid.setItems(workflowEntities);
        }
    }

    private Grid<WorkflowEntity> createMyWorkflowEntitiesGrid() {
        //--------Entity------
        //Created at
        //Workflow
        //Entity Name
        //Workflow status

        //--------Actions-----
        //Tickets ->
            //Current Node
            //Current Responsible
            //Open since # days
        //Workflow
        //History

        Grid<WorkflowEntity> myWorkflowEntitiesGrid = new Grid<>();
        myWorkflowEntitiesGrid.addColumn(wfEntity -> wfEntity.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        myWorkflowEntitiesGrid.addColumn(wfEntity -> wfEntity.getWorkflowType().getName()).setHeader("Workflow").setAutoWidth(true);
        myWorkflowEntitiesGrid.addColumn(WorkflowEntity::getName).setHeader("Name").setAutoWidth(true);
        myWorkflowEntitiesGrid.addColumn(wfEntity -> workflowEntityService.getWorkflowEntityStatus(wfEntity)).setHeader("Status").setAutoWidth(true);
        myWorkflowEntitiesGrid.addComponentColumn(wfEntity -> createActions(wfEntity)).setHeader("Actions").setAutoWidth(true);
        myWorkflowEntitiesGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        myWorkflowEntitiesGrid.setAllRowsVisible(true);
        return myWorkflowEntitiesGrid;
    }

    private HorizontalLayout createActions(WorkflowEntity wfEntity) {
        HorizontalLayout actions = new HorizontalLayout();
        List<WorkflowTicket> tickets = workflowTicketService.getWorkflowTickets(wfEntity.getId());

        Button showTicketsButton = new Button("Tickets");
        showTicketsButton.addClickListener(e -> {
            Dialog dialog = new Dialog();
            Grid<WorkflowTicket> ticketsGrid = new Grid<>(WorkflowTicket.class, false);
            ticketsGrid.setItems(tickets);
            ticketsGrid.addColumn(wfTicket -> workflowNodeService.getById(wfTicket.getCurrentNodeId()).getTitle()).setHeader("Current Node").setAutoWidth(true);
            ticketsGrid.addColumn(wfTicket -> workflowTicketService.getCurrentResponsible(wfTicket.getId()).getName()).setHeader("Current Responsible").setAutoWidth(true);
            ticketsGrid.addColumn(wfTicket -> workflowTicketService.getOpenSinceDays(wfTicket)).setHeader("Open since # days").setAutoWidth(true);
            dialog.add(ticketsGrid);
            dialog.setWidthFull();
            dialog.open();
        });

        Button showHistoryButton = new Button("History");
        showHistoryButton.addClickListener(e -> {
            WorkflowTicketHistoryDialog dialog = new WorkflowTicketHistoryDialog(workflowEntityService.getWorkflowEntityHistory(wfEntity), workflowEntityService);
            dialog.open();
        });

        Button showWorkflowButton = new Button("Workflow");
        showWorkflowButton.addClickListener(e -> {
            Dialog dialog = new Dialog();
            List<WorkflowNode> nodes = workflowNodeService.getAll(wfEntity.getWorkflowType().getId());
            List<ObjectId> nodesWithTicketsIds = workflowTicketService.getCurrentNodes(wfEntity.getId()).stream().map(WorkflowNode::getId).toList();
            dialog.add(new WorkflowView(nodes, nodesWithTicketsIds));
            dialog.open();
        });

        actions.add(showTicketsButton);
        actions.add(showHistoryButton);
        actions.add(showWorkflowButton);
        return actions;
    }
}
