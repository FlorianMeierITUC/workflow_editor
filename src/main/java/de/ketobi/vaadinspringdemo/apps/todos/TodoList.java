package de.ketobi.vaadinspringdemo.apps.todos;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.apps.todos.entities.Todo;
import de.ketobi.vaadinspringdemo.apps.todos.services.TodoService;
import de.ketobi.vaadinspringdemo.main.login.Login;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import de.ketobi.vaadinspringdemo.main.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Route(value = "todos", layout = MainLayout.class)
@PageTitle("My Todos and ideas")
public class TodoList extends VerticalLayout implements BeforeEnterObserver {
    private final TodoService todoService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private TextField name = new TextField("Name *");
    private TextArea description = new TextArea("Description");

    private Grid<Todo> todoGrid;
    private List<Todo> todos = new ArrayList<>();
    private GridListDataView<Todo> todoView;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(UserService.getCurrentUser() == null){
            event.forwardTo(Login.class);
        } else {
            todos = todoService.getAllByUser(UserService.getCurrentUser().getId());
            todoView = todoGrid.setItems(todos);
            todoView.addItemCountChangeListener(e -> Notification.show(e.getItemCount() + " items available"));
        }
    }

    @Autowired
    public TodoList(TodoService todoService, UserService userService){
        this.todoService = todoService;
        this.userService = userService;

        add(new H3("Todos and ideas"));
        add(new H4("Create quick notes and todos here. These are only visible to you and not part of any workflow."));
        add(name);
        add(description);
        add(new SaveTodoButton());

        add(new Hr());
        add(new H4("Todos:"));
        todoGrid = createTodoGrid();
        add(todoGrid);
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
                    todoService.save(todo);
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
        todoGrid.addColumn(todo -> todo.getDoneAt() != null ? todo.getDoneAt().format(formatter) : "").setHeader("Done at").setAutoWidth(true);
        todoGrid.addComponentColumn(todo -> {
            Checkbox doneCheckbox = new Checkbox();
            doneCheckbox.setValue(todo.isDone());
            doneCheckbox.addValueChangeListener(e -> {
                todo.setDone(e.getValue());
                if(e.getValue()){
                    todo.setDoneAt(LocalDateTime.now());
                } else {
                    todo.setDoneAt(null);
                }
                todoService.save(todo);
            });
            return doneCheckbox;
        }).setHeader("Done").setAutoWidth(true);
        todoGrid.addComponentColumn(selectedTodo -> {
            HorizontalLayout actionsLayout = new HorizontalLayout();
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                Dialog dialog = new Dialog();
                VerticalLayout dialogLayout = new VerticalLayout();
                dialog.getHeader().add(new H3("Edit Todo"));
                TextField nameField = new TextField("Name");
                nameField.setValue(selectedTodo.getName());
                TextArea descriptionField = new TextArea("Description");
                descriptionField.setValue(selectedTodo.getDescription());
                Button saveButton = new Button("Save");
                saveButton.addClickListener(saveEvent -> {
                    selectedTodo.setName(nameField.getValue());
                    selectedTodo.setDescription(descriptionField.getValue());
                    todoService.save(selectedTodo);
                    Notification notification = Notification
                            .show("Todo updated!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    dialog.close();
                    todoView.refreshItem(selectedTodo);
                });
                dialogLayout.add(nameField, descriptionField);
                dialog.add(dialogLayout);
                dialog.getFooter().add(saveButton);
                dialog.getElement().getThemeList().add("custom-dialog-overlay");
                dialog.open();
            });
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                todoService.delete(selectedTodo);
                Notification notification = Notification
                        .show("Todo deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                todoView.removeItem(selectedTodo);
            });
            actionsLayout.add(editButton);
            actionsLayout.add(deleteButton);
            return actionsLayout;
        }).setHeader("Actions").setAutoWidth(true);
        todoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_COMPACT);
        todoGrid.setAllRowsVisible(true);
        return todoGrid;
    }
}