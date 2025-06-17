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

    public Ausschreibung save(Ausschreibung ausschreibung) {
        return repo.save(ausschreibung);
    }

    public List<Ausschreibung> findAll() {
        return repo.findAll();
    }


    public void deleteAll() {
        repo.deleteAll();
    }
    
    public Optional<Ausschreibung> findById(String id) {
        return repo.findById(id);
    }
}
