package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.Todo;
import de.ketobi.vaadinspringdemo.entities.User;
import de.ketobi.vaadinspringdemo.entities.Workflow;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class IndexManager {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        // Ensure the unique index is only on the top-level Workflow collection
        mongoTemplate.indexOps(Workflow.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(Todo.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        // Add any other necessary indexes here, ensuring no unique index on embedded documents
    }
}
