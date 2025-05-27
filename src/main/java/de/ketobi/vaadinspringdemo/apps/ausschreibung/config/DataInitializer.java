package de.ketobi.vaadinspringdemo.apps.ausschreibung.config;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.util.SampleData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final AusschreibungService service;

    public DataInitializer(AusschreibungService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1) wipe out old test data
        service.deleteAll();               // ← you’ll need to add this method
        // 2) seed fresh samples
        SampleData
            .createArchivedAusschreibungen()
            .forEach(service::save);
        System.out.println("🔸 Re-seeded sample Ausschreibungen");
    }
}
