package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.Todo;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.entities.WorkflowItem;
import de.ketobi.vaadinspringdemo.repositories.TodoRepository;
import de.ketobi.vaadinspringdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todos", layout = MainLayout.class)
@PageTitle("Todos and ideas")
public class TodoList extends VerticalLayout implements BeforeEnterObserver {
    private final TodoRepository todoRepository;
    private TextField name = new TextField("Name *");
    private TextArea description = new TextArea("Description");
    private GridListDataView<Todo> todoView;
    private GridListDataView<WorkflowItem> workflowTodosView;
    private GridListDataView<WorkflowItem> myWorkflowItemsView;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        }
    }

    @Autowired
    public TodoList(TodoRepository todoRepository){
        this.todoRepository = todoRepository;

        add(new H3("Todos and ideas for this site"));
        add(new Paragraph("New Todo:"));
        add(name);
        add(description);
        add(new SaveTodoButton());

        add(new Paragraph("Todos:"));
        Grid<Todo> todoGrid = createTodoGrid();
        List<Todo> todos = todoRepository.findAll();
        todoView = todoGrid.setItems(todos);
        todoView.addItemCountChangeListener(e -> Notification.show(e.getItemCount() + " items available"));
        add(todoGrid);

        add(new Paragraph("My workflow todos:"));
        Grid<WorkflowItem> workflowTodosGrid = createMyWorkflowTodosGrid();
        //TODO get all workflow items that are assigned to the current user
        List<WorkflowItem> workflowTodos = new ArrayList<>();
        add(new Paragraph("My workflow items:"));
        add(new Span("Not implemented yet"));
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
                todo.setCreatedBy(UserService.getCurrentUser());
                todo.setCreatedAt(LocalDateTime.now());
                try {
                    todoRepository.save(todo);
                    Notification notification = Notification
                            .show("Todo submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    todoView.addItem(todo);
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
        todoGrid.addColumn(todo -> todo.getCreatedBy().getName()).setHeader("Creator").setAutoWidth(true);
        todoGrid.addColumn(Todo::getCreatedAt).setHeader("Created at").setAutoWidth(true);
        todoGrid.addColumn(LitRenderer.<Todo>of("<vaadin-checkbox ?checked=${item.done}></vaadin-checkbox>").withProperty("done", Todo::isDone)).setHeader("Done").setAutoWidth(true);
        todoGrid.addComponentColumn(selectedTodo -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                todoRepository.deleteByName(selectedTodo.getName());
                Notification notification = Notification
                        .show("Todo deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                todoView.removeItem(selectedTodo);
            });
            return deleteButton;
        });
        todoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        return todoGrid;
    }

    private Grid<WorkflowItem> createMyWorkflowTodosGrid() {
        Grid<WorkflowItem> workflowTodosGrid = new Grid<>(WorkflowItem.class, false);
        workflowTodosGrid.addColumn(wfItem -> wfItem.getWorkflow().getName()).setHeader("Workflow").setAutoWidth(true);
        workflowTodosGrid.addColumn(wfItem -> wfItem.getCurrentNode().getTitle()).setHeader("Current Node").setAutoWidth(true);
        workflowTodosGrid.addComponentColumn(selectedTodo -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                //workflowTodosView.removeItem(selectedTodo);
            });
            return editButton;
        });
        workflowTodosGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        return workflowTodosGrid;
    }
}