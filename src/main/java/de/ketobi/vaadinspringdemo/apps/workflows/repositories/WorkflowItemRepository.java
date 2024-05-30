package de.ketobi.vaadinspringdemo.apps.workflows.repositories;

import de.ketobi.vaadinspringdemo.apps.workflows.entities.WorkflowItem;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WorkflowItemRepository<T extends WorkflowItem>{
}
