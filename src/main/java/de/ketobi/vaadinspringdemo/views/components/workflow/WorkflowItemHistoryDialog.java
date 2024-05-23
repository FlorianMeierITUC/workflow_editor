package de.ketobi.vaadinspringdemo.views.components.workflow;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import de.ketobi.vaadinspringdemo.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.entities.WorkflowItemHistory;

import java.util.ArrayList;

public class WorkflowItemHistoryDialog extends Dialog {
    private Grid<WorkflowItemHistory> historyGrid;

    public WorkflowItemHistoryDialog(ArrayList<WorkflowItemHistory> history) {
        historyGrid = new Grid<>(WorkflowItemHistory.class, false);
        historyGrid.setItems(history);
        historyGrid.addColumn(WorkflowItemHistory::getCreatedAt).setHeader("Created At");
        historyGrid.addColumn(historyItem -> historyItem.getWorkflow().getName()).setHeader("Workflow");
        historyGrid.addColumn(historyItem -> historyItem.getNode().getTitle()).setHeader("Node");
        historyGrid.addColumn(historyItem -> historyItem.getItem().getTitle()).setHeader("Item");
        historyGrid.addColumn(WorkflowItemHistory::getMessage).setHeader("Message");
        historyGrid.addColumn(historyItem -> historyItem.getResponsible().getName()).setHeader("Responsible User");
        historyGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);

        add(new Paragraph("Workflow item history"));
        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.getHeader().add(closeButton);
        add(historyGrid);
        setWidthFull();
    }
}