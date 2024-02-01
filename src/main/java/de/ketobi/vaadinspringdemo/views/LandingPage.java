package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collection;

@Route(value = "")
@PageTitle("Landing page")
public class LandingPage extends VerticalLayout {
	
	public LandingPage() {
		setSizeFull();
		add(new Paragraph("Welcome to my landing page!"));
		add(new LoginButton());
		add(new TodosButton());

	}

	private class LoginButton extends Button{
		public LoginButton(){
			setText("Login");
			addClickListener( event -> {
				getUI().ifPresent(ui ->	ui.navigate("login"));
			});
		}
	}

	private class TodosButton extends Button {
		public TodosButton(){
			setText("Todos");
			addClickListener( event -> {
				getUI().ifPresent(ui ->	ui.navigate("todos"));
			});
		}
	}
}
