package de.ketobi.vaadinspringdemo.apps.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.services.DemoObjectService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowTicketHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowTicketService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.bson.types.ObjectId;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "demoobjectlist", layout = MainLayout.class)
@PageTitle("Demo Objects")
public class DemoObjectList extends VerticalLayout implements BeforeEnterObserver {
    private final DemoObjectService demoObjectService;
    private final WorkflowNodeService workflowNodeService;
    private final WorkflowEntityService workflowEntityService;
    private final WorkflowTicketService workflowTicketService;
    private final UserService userService;
    private Grid<DemoObject> demoObjectsGrid;
    private List<DemoObject> demoObjects = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public DemoObjectList(DemoObjectService demoObjectService, WorkflowNodeService workflowNodeService, WorkflowEntityService workflowEntityService, WorkflowTicketService workflowTicketService, UserService userService) {
        this.demoObjectService = demoObjectService;
        this.demoObjectsGrid = createDemoObjectsGrid();
        add(new H3("Demo Objects"));
        add(new H4("These are all the Demo Objects."));
        add(demoObjectsGrid);
        this.workflowNodeService = workflowNodeService;
        this.workflowEntityService = workflowEntityService;
        this.workflowTicketService = workflowTicketService;
        this.userService = userService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            demoObjects = demoObjectService.getAllDemoObjects();
            demoObjectsGrid.setItems(demoObjects);
        }
    }

    private Grid<DemoObject> createDemoObjectsGrid() {
        Grid<DemoObject> demoObjectsGrid = new Grid<>(DemoObject.class, false);
        demoObjectsGrid.addColumn(demoObject -> demoObject.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        demoObjectsGrid.addColumn(DemoObject::getName).setHeader("Name").setAutoWidth(true);
        demoObjectsGrid.addColumn(demoObject -> userService.getUserById(demoObject.getCreatedBy()).getName()).setHeader("Created By").setAutoWidth(true);
        demoObjectsGrid.addColumn(demoObject -> workflowEntityService.getWorkflowEntityStatus(demoObject)).setHeader("Status").setAutoWidth(true);
        demoObjectsGrid.addComponentColumn(selectedDemoObject -> {
            HorizontalLayout buttonDiv = new HorizontalLayout();
            Button workflowButton = new Button("Workflow");
            workflowButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> workflowNodes = workflowNodeService.getAll(WorkflowTypes.DEMO_WORKFLOW.getId());
                List<ObjectId> nodesWithTicketsIds = workflowTicketService.getCurrentNodes(selectedDemoObject.getId()).stream().map(WorkflowNode::getId).toList();
                dialog.add(new WorkflowView(workflowNodes, nodesWithTicketsIds));
                dialog.open();
            });
            Button historyButton = new Button("History");
            historyButton.addClickListener(e -> {
                WorkflowTicketHistoryDialog dialog = new WorkflowTicketHistoryDialog(workflowEntityService.getWorkflowEntityHistory(selectedDemoObject), workflowEntityService);
                dialog.open();
            });
            Button detailsButton = new Button("Details");
            detailsButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                //Add the details of the selectedDemoObject to the dialog
                dialog.add(new H3("Details"));
                dialog.add(new H4("These are the details of the selected Demo Object."));
                dialog.add(new Paragraph("Name: " + selectedDemoObject.getName()));
                dialog.add(new Paragraph("Description: " + selectedDemoObject.getDescription()));
                dialog.add(new Paragraph("Result User Action 1: " + selectedDemoObject.getResultUserAction1()));
                dialog.add(new Paragraph("Result User Action 2: " + selectedDemoObject.getResultUserAction2()));
                dialog.add(new Paragraph("Result Batch Action: " + selectedDemoObject.getResultBatchAction()));
                dialog.add(new Paragraph("Result Batch Action 2: " + selectedDemoObject.getResultBatchAction2()));
                dialog.open();
            });
            buttonDiv.add(workflowButton, historyButton, detailsButton);
            return buttonDiv;
        }).setHeader("Actions");
        demoObjectsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        demoObjectsGrid.setAllRowsVisible(true);
        return demoObjectsGrid;
    }
}