package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.dto.Todo;

import java.util.List;

@Route(value = "todos")
@PageTitle("Todos and ideas")
public class TodoList extends VerticalLayout {
    public TodoList(){
        add(new H3("Todos and ideas for this site"));
        add(new Paragraph("This page collects the ideas and todos for this project. The aim of the project is the creation of a web-application framework monolith. It should contain features like login, user management, persistence and a workflow engine"));
        Grid<Todo> todoGrid = new Grid<>(Todo.class, false);
        todoGrid.addColumn(Todo::getId).setHeader("Number").setAutoWidth(true);
        todoGrid.addColumn(Todo::getName).setHeader("Name").setAutoWidth(true);
        todoGrid.addColumn(Todo::getDescription).setHeader("Description").setAutoWidth(true);
        /*
        grid.addColumn(LitRenderer
       .<Person>of("<b>${item.name}</b>")
       .withProperty("name", Person::getName)
).setHeader("Name");
         */
        //todoGrid.addColumn(Todo::isDone).setHeader("Done").setAutoWidth(true);
        todoGrid.addColumn(LitRenderer.<Todo>of("<input type=checkbox value=${item.done}></input>").withProperty("done", Todo::isDone)).setHeader("Done").setAutoWidth(true);
        todoGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        List<Todo> todos = Todo.getDefaultTodos();
        todoGrid.setItems(todos);
        add(todoGrid);
    }
}