package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Todo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TodoRepository extends MongoRepository<Todo, ObjectId> {
    List<Todo> findByCreatedBy(ObjectId userId);
}
