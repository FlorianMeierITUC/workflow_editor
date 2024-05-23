package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Workflow;
import de.ketobi.vaadinspringdemo.entities.WorkflowTypes;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowRepository extends MongoRepository<Workflow, ObjectId> {
    Workflow findByName(String name);
}
