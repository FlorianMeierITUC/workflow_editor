package de.ketobi.vaadinspringdemo.entities;

import de.ketobi.vaadinspringdemo.repositories.OrderRepository;

public enum WorkflowTypes {
    ORDER_WORKFLOW("Order Workflow", OrderRepository.class);

    private final String name;
    private final Class<?> repository;

    WorkflowTypes(String name, Class<?> repository) {
        this.name = name;
        this.repository = repository;
    }

    public String getName() {
        return name;
    }

    public Class<?> getRepository() {
        return repository;
    }
}
