package de.ketobi.vaadinspringdemo.apps.todos;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.todos.entities.Todo;
import de.ketobi.vaadinspringdemo.apps.todos.repositories.TodoRepository;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import de.ketobi.vaadinspringdemo.main.workflows.components.WorkflowItemHistoryDialog;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItemHistory;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowNode;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowNodeRepository;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowRepository;
import de.ketobi.vaadinspringdemo.main.workflows.services.WorkflowItemService;
import de.ketobi.vaadinspringdemo.main.workflows.viewer.WorkflowView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todos", layout = MainLayout.class)
@PageTitle("Todos and ideas")
public class TodoList extends VerticalLayout implements BeforeEnterObserver {
    private final TodoRepository todoRepository;
    private final WorkflowItemService workflowItemService;
    private final WorkflowRepository workflowRepository;
    private final WorkflowNodeRepository workflowNodeRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private TextField name = new TextField("Name *");
    private TextArea description = new TextArea("Description");

    private Grid<Todo> todoGrid;
    private List<Todo> todos = new ArrayList<>();
    private GridListDataView<Todo> todoView;

    private Grid<WorkflowItem> myWorkflowItemsGrid;
    private List<WorkflowItem> myWorkflowItems = new ArrayList<>();
    private GridListDataView<WorkflowItem> myWorkflowItemsView;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            todos = todoRepository.findByCreatedBy(UserService.getCurrentUser().getId());
            todoView = todoGrid.setItems(todos);
            todoView.addItemCountChangeListener(e -> Notification.show(e.getItemCount() + " items available"));



            myWorkflowItems = workflowItemService.getAllWorkflowItemsCreatedByTheCurrentUser();
            myWorkflowItemsView = myWorkflowItemsGrid.setItems(myWorkflowItems);
        }
    }

    @Autowired
    public TodoList(TodoRepository todoRepository, WorkflowItemService workflowItemService, WorkflowRepository workflowRepository, WorkflowNodeRepository workflowNodeRepository, UserService userService, MongoTemplate mongoTemplate){
        this.todoRepository = todoRepository;
        this.workflowItemService = workflowItemService;
        this.workflowRepository = workflowRepository;
        this.workflowNodeRepository = workflowNodeRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;

        add(new H3("Todos and ideas for this site"));
        add(new H4("New Todo:"));
        add(name);
        add(description);
        add(new SaveTodoButton());

        add(new Hr());
        add(new H4("Todos:"));
        todoGrid = createTodoGrid();
        add(todoGrid);

        add(new Hr());
        add(new H4("My workflow items:"));
        myWorkflowItemsGrid = createMyWorkflowItemsGrid();
        add(myWorkflowItemsGrid);
    }

    private class SaveTodoButton extends Button {
        SaveTodoButton(){
            setText("+ Add");
            addSingleClickListener(clickEvent -> {
                Todo todo = new Todo();
                todo.setName(name.getValue());
                if(null == name.getValue() || name.getValue().isEmpty() || name.getValue().isBlank()){
                    Notification notification = Notification
                            .show("Please provide a name for the todo!");
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                    return;
                }
                todo.setDescription(description.getValue());
                todo.setCreatedBy(UserService.getCurrentUser().getId());
                todo.setCreatedAt(LocalDateTime.now());
                try {
                    todoRepository.save(todo);
                    Notification notification = Notification
                            .show("Todo submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    todoView.addItem(todo);
                    name.clear();
                    description.clear();
                } catch (DuplicateKeyException ex){
                    Notification notification = Notification
                            .show("Entry with this name already present! Choose a different name!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
        }

    }

    private Grid<Todo> createTodoGrid(){
        Grid<Todo> todoGrid = new Grid<>(Todo.class, false);
        todoGrid.addColumn(Todo::getName).setHeader("Name").setAutoWidth(true);
        todoGrid.addColumn(Todo::getDescription).setHeader("Description").setAutoWidth(true);
        todoGrid.addColumn(todo -> userService.getUserById(todo.getCreatedBy()).getName()).setHeader("Creator").setAutoWidth(true);
        todoGrid.addColumn(todo -> todo.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        todoGrid.addColumn(LitRenderer.<Todo>of("<vaadin-checkbox ?checked=${item.done}></vaadin-checkbox>").withProperty("done", Todo::isDone)).setHeader("Done").setAutoWidth(true);
        todoGrid.addComponentColumn(selectedTodo -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                todoRepository.delete(selectedTodo);
                Notification notification = Notification
                        .show("Todo deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                todoView.removeItem(selectedTodo);
            });
            return deleteButton;
        });
        todoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        todoGrid.setAllRowsVisible(true);
        return todoGrid;
    }

    private Grid<WorkflowItem> createMyWorkflowItemsGrid() {
        Grid<WorkflowItem> myWorkflowItemsGrid = new Grid<>(WorkflowItem.class, false);
        myWorkflowItemsGrid.addColumn(wfItem -> wfItem.getCreatedAt().format(formatter)).setHeader("Created at").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(wfItem -> workflowRepository.findById(wfItem.getWorkflowId()).orElseThrow().getName()).setHeader("Workflow").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(wfItem -> workflowNodeRepository.findById(wfItem.getCurrentNode()).get().getTitle()).setHeader("Current Node").setAutoWidth(true);
        myWorkflowItemsGrid.addColumn(WorkflowItem::getTitle).setHeader("Title").setAutoWidth(true);
        myWorkflowItemsGrid.addComponentColumn(selectedWfItem -> {
            Div buttonDiv = new Div();
            Button editButton = new Button("Show workflow");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                List<WorkflowNode> nodes = workflowNodeRepository.findByIdWorkflow(selectedWfItem.getWorkflowId());
                dialog.add(new WorkflowView(nodes, workflowNodeRepository.findById(selectedWfItem.getCurrentNode()).orElseThrow()));
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