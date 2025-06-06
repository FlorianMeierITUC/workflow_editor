package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;

@Route(value = "onepager", layout = MainLayout.class)
@PageTitle("Onepager") 
public class OnePager extends VerticalLayout {
    
    public OnePager() {
        
        H1 header = new H1("You can create the one-pager here");
        header.getStyle().set("text-align", "center");
        
        add(header);
        
    }

}
