package de.ketobi.vaadinspringdemo.main.ui.navigation;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.component.html.H3;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungView;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.views.WelcomePage;


@SpringComponent
@UIScope
public class NavigationLayout extends VerticalLayout {

    public NavigationLayout() {
        // Defer RouterLink creation until Vaadin UI is attached
        addAttachListener(event -> buildSidebar());
    }

    private void buildSidebar() {
        removeAll(); // clear in case this is triggered more than once

        // Add a title before the navigation links
        H3 dashboard = new H3("Dahsboard");
        H3 messages = new H3("Messages");
        H3 chatBot = new H3("Chat BOT");

        RouterLink chatbotLink = new RouterLink("Chat", WelcomePage.class); //TODO: Change to the chat link 
        
        H3 ausschreibung = new H3("Ausschreibung APP");
        RouterLink ausschreibungLink = new RouterLink("Ausschreibungs", AusschreibungView.class);
        
        add(dashboard, messages, chatBot, chatbotLink, ausschreibung, ausschreibungLink);
    }

     public void refresh(){
        removeAll();
        buildSidebar(); // Rebuild the sidebar to reflect any changes
    }
}
