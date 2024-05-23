package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String>{
    List<Order> findByCreatedBy(String userName);
}
