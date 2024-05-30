package de.ketobi.vaadinspringdemo.apps.workflows.repositories;

import de.ketobi.vaadinspringdemo.apps.workflows.entities.Workflow;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkflowRepository extends MongoRepository<Workflow, ObjectId> {
    Workflow findByName(String name);
}
