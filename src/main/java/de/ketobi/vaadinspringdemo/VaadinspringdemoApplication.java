package de.ketobi.vaadinspringdemo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Theme(value = "my-theme", variant = Lumo.DARK)
public class VaadinspringdemoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(VaadinspringdemoApplication.class, args);
	}

}
