package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WorkflowRepository extends MongoRepository<Workflow, ObjectId> {
    Optional<Workflow> findByName(String name);
}
