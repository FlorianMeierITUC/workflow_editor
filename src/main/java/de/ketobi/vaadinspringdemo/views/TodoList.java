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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.entities.Todo;
import de.ketobi.vaadinspringdemo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import java.util.ArrayList;

@Route(value = "todos")
@PageTitle("Todos and ideas")
public class TodoList extends VerticalLayout {
    private final TodoRepository todoRepository;
    private TextField name = new TextField("Name");
    private TextArea description = new TextArea("Description");
    private ArrayList<Todo> todos;
    private GridListDataView<Todo> todoView;
    @Autowired
    public TodoList(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
        this.todos = new ArrayList<>(todoRepository.findAll());
        Grid<Todo> todoGrid = new Grid<>(Todo.class, false);
        todoGrid.addColumn(Todo::getName).setHeader("Name").setAutoWidth(true);
        todoGrid.addColumn(Todo::getDescription).setHeader("Description").setAutoWidth(true);
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

        todoView = todoGrid.setItems(todos);

        add(new H3("Todos and ideas for this site"));
        add(new Paragraph("This page collects the ideas and todos for this project. The aim of the project is the creation of a web-application framework monolith. It should contain features like login, user management, persistence and a workflow engine"));
        add(new Paragraph("New Todo:"));
        add(name);
        add(description);
        add(new SaveButton());
        todoView.addItemCountChangeListener(e ->
                Notification.show(" " + e.getItemCount() + " items available"));

        Span itemCountSpan = new Span("Total Item Count: " + todoView.getItemCount());
        add(itemCountSpan);
        add(todoGrid);
    }

    private class SaveButton extends Button {
        SaveButton(){
            setText("+ Add");
            addSingleClickListener(clickEvent -> {
                Todo todo = new Todo();
                todo.setName(name.getValue());
                todo.setDescription(description.getValue());
                //TODO Replace with real user
                todo.setCreatedBy("Tobias");
                try {
                    todoRepository.save(todo);
                    Notification notification = Notification
                            .show("Todo submitted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    //TODO get the list to refresh itself WTF?
                    todoView.addItem(todo);


                } catch (DuplicateKeyException ex){
                    Notification notification = Notification
                            .show("Entry with this name already present! Choose a different name!");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });
        }

    }
}