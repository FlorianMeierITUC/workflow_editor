package de.ketobi.vaadinspringdemo.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLayout;

import java.util.Collection;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Landing page")
public class LandingPage extends VerticalLayout {

	public LandingPage() {
		add(new Paragraph("Welcome to my landing page!"));
	}
}