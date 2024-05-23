package de.ketobi.vaadinspringdemo.repositories;

import de.ketobi.vaadinspringdemo.entities.WorkflowItem;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WorkflowItemRepository<T extends WorkflowItem>{
}
