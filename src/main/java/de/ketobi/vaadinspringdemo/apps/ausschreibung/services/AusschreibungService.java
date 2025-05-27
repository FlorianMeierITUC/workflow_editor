package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.repositories.AusschreibungRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AusschreibungService {
    private final AusschreibungRepository repo;

    public AusschreibungService(AusschreibungRepository repo) {
        this.repo = repo;
    }

    /** Persist any Ausschreibung (e.g. toggling favorite) */
    public Ausschreibung save(Ausschreibung ausschreibung) {
        return repo.save(ausschreibung);
    }

    /** Fetch all Ausschreibungen (if you ever need them) */
    public List<Ausschreibung> findAll() {
        return repo.findAll();
    }

    /** Fetch only those Ausschreibungen where archived == true */
    // public List<Ausschreibung> findArchived() {
    //     return repo.findByArchivedTrue();
    // }

    public void deleteAll() {
        repo.deleteAll();
    }
    
    /** (Optional) Lookup a single Ausschreibung by its ID */
    public Optional<Ausschreibung> findById(String id) {
        return repo.findById(id);
    }
}
