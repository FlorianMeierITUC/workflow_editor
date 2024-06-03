package de.ketobi.vaadinspringdemo.apps.orders.repositories;

import de.ketobi.vaadinspringdemo.apps.orders.entities.Order;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowItemRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    List<Order> findByCreatedBy(ObjectId userId);
}
