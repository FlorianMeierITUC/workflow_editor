package de.ketobi.vaadinspringdemo.apps.todos.services;

import de.ketobi.vaadinspringdemo.apps.todos.entities.Todo;
import de.ketobi.vaadinspringdemo.apps.todos.repositories.TodoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public ArrayList<Todo> getAllByUser(ObjectId id) {
        return new ArrayList<>(todoRepository.findByCreatedBy(id));
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public void delete(Todo todo) {
        todoRepository.delete(todo);
    }
}
