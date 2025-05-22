package de.ketobi.vaadinspringdemo.apps.ausschreibung.repositories;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AusschreibungRepository extends MongoRepository<Ausschreibung, String> {
    //List<Ausschreibung> findByArchivedTrue();
}
