package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "workflowentities", layout = MainLayout.class)
@PageTitle("My Workflow Entities")
public class WorkflowEntitiesList extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowEntityService workflowEntityService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private Grid<WorkflowEntity> myWorkflowEntitiesGrid;
    private List<WorkflowEntity> myWorkflowEntities = new ArrayList<>();
    private GridListDataView<WorkflowEntity> myWorkflowEntitiesView;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public WorkflowEntitiesList(WorkflowEntityService workflowEntityService, WorkflowService workflowService, WorkflowNodeService workflowNodeService) {
        this.workflowEntityService = workflowEntityService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
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
            myWorkflowEntities = workflowEntityService.getAllWorkflowEntitiesCreatedByTheCurrentUser();
            myWorkflowEntitiesView = myWorkflowEntitiesGrid.setItems(myWorkflowEntities);
        }
    }

    private Grid<WorkflowEntity> createMyWorkflowEntitiesGrid() {

        Grid<WorkflowEntity> myWorkflowEntitiesGrid = new Grid<>(WorkflowEntity.class, false);
        myWorkflowEntitiesGrid.addColumn(wfEntity -> wfEntity.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        myWorkflowEntitiesGrid.addColumn(wfEntity -> workflowService.getById(wfEntity.getWorkflowType().getId()).getName()).setHeader("Workflow").setAutoWidth(true);
        //TODO use a hierarchical grid to display sibling tickets
        //myWorkflowEntitiesGrid.addColumn(wfItem -> workflowNodeService.getById(wfItem.getCurrentNode()).getTitle()).setHeader("Current Node").setAutoWidth(true);
        myWorkflowEntitiesGrid.addColumn(WorkflowEntity::getName).setHeader("Name").setAutoWidth(true);
        myWorkflowEntitiesGrid.addComponentColumn(selectedWfEntity -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Show workflow");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> nodes = workflowNodeService.getAll(selectedWfEntity.getWorkflowType().getId());
                //TODO implement the highlighting of several nodes (if this makes sense here)
                //dialog.add(new WorkflowView(nodes, workflowNodeService.getById(selectedWfEntity.getCurrentNode())));
                dialog.open();
            });
            Button historyButton = new Button("Show history");
            historyButton.addClickListener(e -> {
                WorkflowTicketHistoryDialog dialog = new WorkflowTicketHistoryDialog(workflowEntityService.getWorkflowItemHistory(selectedWfEntity), workflowEntityService);
                dialog.open();
            });
            buttonDiv.add(editButton);
            buttonDiv.add(historyButton);
            return buttonDiv;
        });
        myWorkflowEntitiesGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        myWorkflowEntitiesGrid.setAllRowsVisible(true);
        return myWorkflowEntitiesGrid;
    }
}
