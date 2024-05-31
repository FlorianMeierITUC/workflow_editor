package de.ketobi.vaadinspringdemo.main.workflows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowItemHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItemHistory;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowNodeService;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowService;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "workflowtickets", layout = MainLayout.class)
@PageTitle("My Workflow Tickets")
public class WorkflowTicketList  extends VerticalLayout implements BeforeEnterObserver {
    private final WorkflowItemService workflowItemService;
    private final WorkflowService workflowService;
    private final WorkflowNodeService workflowNodeService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private Grid<WorkflowItem> workflowTicketsGrid;
    private List<WorkflowItem> workflowTickets = new ArrayList<>();
    private GridListDataView<WorkflowItem> workflowTicketsView;

    public WorkflowTicketList(
            WorkflowItemService workflowItemService,
            WorkflowService workflowService,
            WorkflowNodeService workflowNodeService,
            UserService userService,
            MongoTemplate mongoTemplate){
        this.workflowItemService = workflowItemService;
        this.workflowService = workflowService;
        this.workflowNodeService = workflowNodeService;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;

        workflowTicketsGrid = createMyWorkflowTicketsGrid();
        workflowTicketsView = workflowTicketsGrid.setItems(workflowTickets);
        add(workflowTicketsGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            workflowTickets = workflowItemService.getAllWorkflowItemsAssignedToTheCurrentUser();
            workflowTicketsView = workflowTicketsGrid.setItems(workflowTickets);
        }
    }

    private Grid<WorkflowItem> createMyWorkflowTicketsGrid() {
        Grid<WorkflowItem> workflowTicketsGrid = new Grid<>(WorkflowItem.class, false);
        workflowTicketsGrid.addColumn(wfItem -> workflowService.getById(wfItem.getWorkflowId()).getName()).setHeader("Workflow").setAutoWidth(true);
        workflowTicketsGrid.addColumn(wfItem -> workflowNodeService.getById(wfItem.getCurrentNode()).getTitle()).setHeader("Current Node").setAutoWidth(true);
        workflowTicketsGrid.addColumn(WorkflowItem::getTitle).setHeader("Title").setAutoWidth(true);
        workflowTicketsGrid.addComponentColumn(selectedWfItem -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                WorkflowNode node = workflowNodeService.getById(selectedWfItem.getCurrentNode());
                getUI().ifPresent(ui -> ui.navigate("/"+node.getId()+"/"+selectedWfItem.getId().toString()));
            });
            Button historyButton = new Button("Show history");
            historyButton.addClickListener(e -> {
                WorkflowItemHistoryDialog dialog = new WorkflowItemHistoryDialog(workflowItemService.getWorkflowItemHistory(selectedWfItem));
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
                    selectedWfItem.setCurrentResponsible(responsible.getValue().getId());
                    selectedWfItem.setMongoTemplate(mongoTemplate);
                    selectedWfItem.save();
                    User user = UserService.getCurrentUser();
                    Workflow wf = workflowService.getById(selectedWfItem.getWorkflowId());
                    WorkflowNode node = workflowNodeService.getById(selectedWfItem.getCurrentNode());
                    WorkflowItemHistory history = WorkflowItemHistory.builder()
                            .itemId(selectedWfItem.getId())
                            .workflowName(wf.getName())
                            .nodeTitle(node.getTitle())
                            .itemTitle(selectedWfItem.getTitle())
                            .message("Item forwarded: "+user.getName()+" -> "+responsible.getValue().getName()+". Message: "+message.getValue())
                            .responsibleUser(user.getName())
                            .createdAt(LocalDateTime.now())
                            .build();
                    workflowItemService.writeWorkflowHistoryEntry(history);
                    workflowTicketsView.removeItem(selectedWfItem);
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
            buttonDiv.add(historyButton);
            return buttonDiv;
        });
        workflowTicketsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        workflowTicketsGrid.setAllRowsVisible(true);
        return workflowTicketsGrid;
    }

}
