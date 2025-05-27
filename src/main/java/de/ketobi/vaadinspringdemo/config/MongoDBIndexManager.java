package de.ketobi.vaadinspringdemo.config;

import de.ketobi.vaadinspringdemo.main.user.entities.User;
import de.ketobi.vaadinspringdemo.main.workflows.entities.Workflow;
import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowSchedule;
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
        if (!mongoTemplate.collectionExists(Workflow.class)) {
            mongoTemplate.indexOps(Workflow.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        }

        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.indexOps(User.class).ensureIndex(new Index().on("name", Sort.Direction.ASC).unique());
        }

        if (!mongoTemplate.collectionExists(WorkflowSchedule.class)) {
            mongoTemplate.indexOps(WorkflowSchedule.class).ensureIndex(new Index().on("workflowId", Sort.Direction.ASC));
        }
    }
}

