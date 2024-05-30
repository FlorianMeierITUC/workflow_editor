package de.ketobi.vaadinspringdemo.main.workflows.repositories;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowItem;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WorkflowItemRepository<T extends WorkflowItem>{
}
