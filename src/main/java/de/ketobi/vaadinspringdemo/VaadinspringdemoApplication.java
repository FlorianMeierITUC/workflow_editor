package de.ketobi.vaadinspringdemo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.page.Push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

// Enable push
@Push
@SpringBootApplication
@Theme(value = "my-theme", variant = Lumo.DARK)
public class VaadinspringdemoApplication implements AppShellConfigurator {

	private static ApplicationContext applicationContext;

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(VaadinspringdemoApplication.class, args);
		VaadinspringdemoApplication.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
