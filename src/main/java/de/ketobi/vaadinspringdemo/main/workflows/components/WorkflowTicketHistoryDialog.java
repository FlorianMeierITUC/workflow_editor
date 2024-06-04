package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicketHistory;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTypes;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowEntityService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkflowTicketHistoryDialog extends Dialog {
    private Grid<WorkflowTicketHistory> historyGrid;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public WorkflowTicketHistoryDialog(ArrayList<WorkflowTicketHistory> history, WorkflowEntityService workflowEntityService) {
        historyGrid = new Grid<>(WorkflowTicketHistory.class, false);
        historyGrid.setItems(history);
        historyGrid.addColumn(wfItemHistory -> wfItemHistory.getCreatedAt().format(formatter)).setHeader("Created At");
        historyGrid.addColumn(WorkflowTicketHistory::getWorkflowName).setHeader("Workflow");
        historyGrid.addColumn(WorkflowTicketHistory::getNodeTitle).setHeader("Node");
        historyGrid.addColumn(wfItemHistory -> workflowEntityService.getWorkflowEntity(wfItemHistory.getEntityId(), WorkflowTypes.fromName(wfItemHistory.getWorkflowName()).getId()).getName()).setHeader("Entity name");
        historyGrid.addColumn(WorkflowTicketHistory::getMessage).setHeader("Message");
        historyGrid.addColumn(WorkflowTicketHistory::getResponsibleUser).setHeader("Responsible User");
        historyGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        historyGrid.setAllRowsVisible(true);

        add(new Paragraph("Workflow item history"));
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.getHeader().add(closeButton);

        add(historyGrid);
        setWidthFull();
    }
}