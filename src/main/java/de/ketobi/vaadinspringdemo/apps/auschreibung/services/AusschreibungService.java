package de.ketobi.vaadinspringdemo.apps.auschreibung.services;

import de.ketobi.vaadinspringdemo.apps.auschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.auschreibung.repositories.AusschreibungRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AusschreibungService {
    private final AusschreibungRepository repo;

    public AusschreibungService(AusschreibungRepository repo) {
        this.repo = repo;
    }

    public void save(Ausschreibung ausschreibung) {
        repo.save(ausschreibung);
    }

    public List<Ausschreibung> findAll() {
        return repo.findAll();
    }
}
