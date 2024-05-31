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
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowItemHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "workflowitems", layout = MainLayout.class)
@PageTitle("My Workflow Items")
public class WorkflowItemsList extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowItemService workflowItemService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private Grid<WorkflowItem> myWorkflowItemsGrid;
    private List<WorkflowItem> myWorkflowItems = new ArrayList<>();
    private GridListDataView<WorkflowItem> myWorkflowItemsView;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public WorkflowItemsList(WorkflowItemService workflowItemService, WorkflowService workflowService, WorkflowNodeService workflowNodeService) {
        this.workflowItemService = workflowItemService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.myWorkflowItemsGrid = createMyWorkflowItemsGrid();

        add(new H3("Workflow items"));
        add(new H4("The workflow items i created across all workflows."));
        add(myWorkflowItemsGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            myWorkflowItems = workflowItemService.getAllWorkflowItemsCreatedByTheCurrentUser();
            myWorkflowItemsView = myWorkflowItemsGrid.setItems(myWorkflowItems);
        }
    }

    private Grid<WorkflowItem> createMyWorkflowItemsGrid() {
        Grid<WorkflowItem> myWorkflowItemsGrid = new Grid<>(WorkflowItem.class, false);
        myWorkflowItemsGrid.addColumn(wfItem -> wfItem.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(wfItem -> workflowService.getById(wfItem.getWorkflowId()).getName()).setHeader("Workflow").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(wfItem -> workflowNodeService.getById(wfItem.getCurrentNode()).getTitle()).setHeader("Current Node").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(WorkflowItem::getTitle).setHeader("Title").setAutoWidth(true);
        myWorkflowItemsGrid.addComponentColumn(selectedWfItem -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Show workflow");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> nodes = workflowNodeService.getAll(selectedWfItem.getWorkflowId());
                dialog.add(new WorkflowView(nodes, workflowNodeService.getById(selectedWfItem.getCurrentNode())));
                dialog.open();
            });
            Button historyButton = new Button("Show history");
            historyButton.addClickListener(e -> {
                WorkflowItemHistoryDialog dialog = new WorkflowItemHistoryDialog(workflowItemService.getWorkflowItemHistory(selectedWfItem));
                dialog.open();
            });
            buttonDiv.add(editButton);
            buttonDiv.add(historyButton);
            return buttonDiv;
        });
        myWorkflowItemsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        myWorkflowItemsGrid.setAllRowsVisible(true);
        return myWorkflowItemsGrid;
    }
}
