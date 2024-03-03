package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TodoRepository extends MongoRepository<Todo, String> {
    public Todo findByName(String name);
    public List<Todo> findByCreatedBy(String userName);
    public void deleteByName(String name);
}
