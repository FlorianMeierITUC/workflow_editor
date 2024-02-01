package de.ketobi.vaadinspringdemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Todo {
    @Getter
    @Setter
    int id;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String description;

    @Getter
    @Setter
    boolean done;

    public Todo(int id, String name, String description, boolean done){
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
    }
    public static ArrayList<Todo> getDefaultTodos(){
        ArrayList<Todo> defaultTodos = new ArrayList<>();
        defaultTodos.add(new Todo(1, "Security", "Implement google login and spring security.", false));
        defaultTodos.add(new Todo(2, "User list", "Implement a list of known users", false));
        defaultTodos.add(new Todo(3, "Database", "Implement MongoDB to store user and todos.", false));
        defaultTodos.add(new Todo(4, "Roles", "Implement user and admin roles.", false));
        defaultTodos.add(new Todo(5, "Workflow engine", "Implement a workflow engine that can send tickets to users, implement group tasks, a workflow editor and recurring tasks.", false));
        defaultTodos.add(new Todo(6, "Server", "Deploy the application on ketobi.de.", false));
        defaultTodos.add(new Todo(7, "GitHub", "Create a github repository.", false));
        defaultTodos.add(new Todo(8, "CI/CD", "Implement a CI/CD chain with GitHub Actions.", false));
        defaultTodos.add(new Todo(9, "Database API", "Use JPA / Hibernate as persistence API", false));
        defaultTodos.add(new Todo(10, "Styling", "Use application wide styling to make everything smaller", false));
        defaultTodos.add(new Todo(11, "Scalability", "Define on concept on how scalable the monolith is. (Containers, Modules, Databases, etc.", false));
        defaultTodos.add(new Todo(12, "Documentation", "Document all configuration and setup steps that are not included in the code.", false));

        return defaultTodos;
    }
}
