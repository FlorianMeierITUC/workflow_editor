package de.ketobi.vaadinspringdemo.main.user.repositories;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByName(String name);
}
