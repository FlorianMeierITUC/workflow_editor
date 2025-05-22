package de.ketobi.vaadinspringdemo.apps.auschreibung.repositories;

import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AusschreibungRepository extends MongoRepository<Ausschreibung, String> {
    //List<Ausschreibung> findByArchivedTrue();
}
