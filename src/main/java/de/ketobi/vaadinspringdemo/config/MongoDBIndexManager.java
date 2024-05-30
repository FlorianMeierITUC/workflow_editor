package de.ketobi.vaadinspringdemo.config;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.apps.workflows.entities.Workflow;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class MongoDBIndexManager {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        // Ensure the unique index is only on the top-level Workflow collection
        mongoTemplate.indexOps(Workflow.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        // Add any other necessary indexes here, ensuring no unique index on embedded documents
    }
}
