package de.ketobi.vaadinspringdemo.apps.demo.repositories;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoObjectRepository extends MongoRepository<DemoObject, ObjectId> {
}