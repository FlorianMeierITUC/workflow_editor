package de.ketobi.vaadinspringdemo.main.user.repositories;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByName(String name);
    ArrayList<User> findAllBySubstituteUserId(ObjectId substituteUserId);
}
