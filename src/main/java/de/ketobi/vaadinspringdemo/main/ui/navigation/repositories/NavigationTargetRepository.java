package de.ketobi.vaadinspringdemo.main.ui.navigation.repositories;

import de.ketobi.vaadinspringdemo.main.ui.navigation.entities.NavigationTarget;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NavigationTargetRepository extends MongoRepository<NavigationTarget, ObjectId>{
    List<NavigationTarget> findByIdFolder(ObjectId folderId);
}
