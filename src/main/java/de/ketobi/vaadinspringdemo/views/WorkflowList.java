package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.repositories.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;

@Route(value = "workflows", layout = MainLayout.class)
@PageTitle("Workflows")
public class WorkflowList extends VerticalLayout {
    private WorkflowRepository workflowRepository;
    private GridListDataView<Workflow> workflowView;
    private TextField name = new TextField("Name *");
    private TextArea description = new TextArea("Description");
    @Autowired
    public WorkflowList(WorkflowRepository workflowRepository){
        this.workflowRepository = workflowRepository;
        ArrayList<Workflow> workflowList = new ArrayList<>(workflowRepository.findAll());
        Grid<Workflow> wfGrid = new Grid<>(Workflow.class, false);
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
                workflowRepository.delete(selectedWf);
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
        add(new Paragraph("Create or display workflows"));
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
                //TODO Replace with real user
                wf.setCreatedBy("Tobias");
                try {
                    workflowRepository.save(wf);
                    Notification notification = Notification
                            .show("Workflow submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    workflowView.addItem(wf);
                } catch (DuplicateKeyException ex) {
                    Notification notification = Notification
                            .show("Entry with this name already present! Choose a different name!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
        }
    }
}
