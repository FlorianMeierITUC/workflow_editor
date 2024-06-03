package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNodeTypes;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;

@Route(value = "workflows", layout = MainLayout.class)
@PageTitle("Workflows")
public class WorkflowList extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private GridListDataView<Workflow> workflowView;
    private TextField name = new TextField("Name *");
    private TextArea description = new TextArea("Description");

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }

    @Autowired
    public WorkflowList(WorkflowService workflowService, WorkflowNodeService workflowNodeService){
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        ArrayList<Workflow> workflowList = new ArrayList<>(workflowService.findAll());
        Grid<Workflow> wfGrid = new Grid<>(Workflow.class, false);
        wfGrid.addColumn(Workflow::getId).setHeader("ID").setAutoWidth(true);
        wfGrid.addColumn(Workflow::getName).setHeader("Name").setAutoWidth(true);
        wfGrid.addColumn(Workflow::getDescription).setHeader("Description").setAutoWidth(true);
        wfGrid.addComponentColumn(selectedWf -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                editButton.getUI().ifPresent(ui ->
                        ui.navigate(WorkflowEditor.class, selectedWf.getId().toString()));

            });
            return editButton;
        });
        wfGrid.addComponentColumn(selectedWf -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                workflowNodeService.deleteAllFromWorkflow(selectedWf.getId());
                workflowService.delete(selectedWf);
                Notification notification = Notification
                        .show("Workflow deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                workflowView.removeItem(selectedWf);
            });
            return deleteButton;
        });
        wfGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        workflowView = wfGrid.setItems(workflowList);

        add(new H3("List of available workflows"));
        add(new H4("Create and display workflows"));
        add(new Paragraph("New Workflow:"));
        add(name);
        add(description);
        add(new SaveButton());
        add(wfGrid);
    }

    private class SaveButton extends Button {
        SaveButton() {
            setText("+ Add");
            addSingleClickListener(clickEvent -> {
                Workflow wf = new Workflow();
                wf.setName(name.getValue());
                if(null == name.getValue() || name.getValue().isEmpty() || name.getValue().isBlank()){
                    Notification notification = Notification
                            .show("Please provide a name for the workflow!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                    return;
                }
                wf.setDescription(description.getValue());
                wf.setActive(true);
                wf.setCreatedBy(UserService.getCurrentUser());
                try {
                    workflowService.save(wf);
                    //Create start node
                    WorkflowNode startNode = new WorkflowNode();
                    startNode.setIdWorkflow(wf.getId());
                    startNode.setTitle("Start");
                    startNode.setType(WorkflowNodeTypes.START);
                    startNode.setResponsible(UserService.getSystemUser().getId());
                    workflowNodeService.save(startNode);

                    //Create end node
                    WorkflowNode endNode = new WorkflowNode();
                    endNode.setIdWorkflow(wf.getId());
                    endNode.setTitle("End");
                    endNode.setType(WorkflowNodeTypes.END);
                    endNode.setResponsible(UserService.getSystemUser().getId());
                    //Set start node as predecessor of end node
                    ArrayList<ObjectId> predecessors = new ArrayList<>();
                    predecessors.add(startNode.getId());
                    endNode.setPredecessorNodes(predecessors);
                    //Save end node
                    workflowNodeService.save(endNode);

                    //Set end node as successor of start node
                    ArrayList<ObjectId> successors = new ArrayList<>();
                    successors.add(endNode.getId());
                    startNode.setSuccessorNodes(successors);

                    workflowNodeService.save(startNode);
                    Notification notification = Notification
                            .show("Workflow submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    workflowView.addItem(wf);
                } catch (DuplicateKeyException ex) {
                    Notification notification = Notification
                            .show("Entry with this name already present! Choose a different name!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    Notification notification = Notification
                            .show("An error occurred while saving the workflow!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    ex.printStackTrace();
                }
            });
        }
    }
}
