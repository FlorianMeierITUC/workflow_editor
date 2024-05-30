package de.ketobi.vaadinspringdemo.main.workflows.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItemHistory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkflowItemHistoryDialog extends Dialog {
    private Grid<WorkflowItemHistory> historyGrid;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public WorkflowItemHistoryDialog(ArrayList<WorkflowItemHistory> history) {
        historyGrid = new Grid<>(WorkflowItemHistory.class, false);
        historyGrid.setItems(history);
        historyGrid.addColumn(wfItemHistory -> wfItemHistory.getCreatedAt().format(formatter)).setHeader("Created At");
        historyGrid.addColumn(WorkflowItemHistory::getWorkflowName).setHeader("Workflow");
        historyGrid.addColumn(WorkflowItemHistory::getNodeTitle).setHeader("Node");
        historyGrid.addColumn(WorkflowItemHistory::getItemTitle).setHeader("Item");
        historyGrid.addColumn(WorkflowItemHistory::getMessage).setHeader("Message");
        historyGrid.addColumn(WorkflowItemHistory::getResponsibleUser).setHeader("Responsible User");
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