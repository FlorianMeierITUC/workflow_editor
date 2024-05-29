package de.ketobi.vaadinspringdemo.main.navigation.repositories;

import de.ketobi.vaadinspringdemo.main.navigation.entities.NavigationFolder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavigationFolderRepository extends MongoRepository<NavigationFolder, ObjectId>{

}
