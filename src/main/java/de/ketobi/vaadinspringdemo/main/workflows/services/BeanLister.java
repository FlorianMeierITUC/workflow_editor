package de.ketobi.vaadinspringdemo.main.workflows.services;

import org.springframework.context.ApplicationContext;

import de.ketobi.vaadinspringdemo.VaadinspringdemoApplication;

public class BeanLister {

    // For some reason @Autowired doesn't work here
    private ApplicationContext applicationContext = VaadinspringdemoApplication.getApplicationContext();

    public String[] listAllBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        return beanNames;
    }
}
