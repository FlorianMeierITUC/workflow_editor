package de.ketobi.vaadinspringdemo.apps.demo.services;

import de.ketobi.vaadinspringdemo.apps.demo.entities.DemoObject;
import de.ketobi.vaadinspringdemo.apps.demo.repositories.DemoObjectRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoObjectService {

    private final DemoObjectRepository demoObjectRepository;

    @Autowired
    public DemoObjectService(DemoObjectRepository demoObjectRepository) {
        this.demoObjectRepository = demoObjectRepository;
    }

    public List<DemoObject> getAllDemoObjects() {
        return demoObjectRepository.findAll();
    }

    public DemoObject save(DemoObject demoObject) {
        return demoObjectRepository.save(demoObject);
    }

    public DemoObject getById(ObjectId itemId) {
        return demoObjectRepository.findById(itemId).orElseThrow();
    }

    public DemoObject getById(String itemId) {
        return demoObjectRepository.findById(new ObjectId(itemId)).orElseThrow();
    }

}