package de.ketobi.vaadinspringdemo.apps.ausschreibung.views;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.components.IconGalleryComponent;
import de.ketobi.vaadinspringdemo.main.ui.MainLayout;
import com.vaadin.flow.component.html.Image;

@Route(value = "home", layout = MainLayout.class)
@PageTitle("Welcome")
public class WelcomePage extends VerticalLayout {

    public WelcomePage() {
        // Add an image at the top of the page
        Image logo = new Image("/images/logo.png", "Logo");
        logo.getStyle().set("margin", "2rem auto").set("display", "block").set("height", "10rem").set("width", "10rem");
        add(logo);
        H2 welcomeHeader = new H2("Hey! Welche Fragen hast du heute an mich?");
        welcomeHeader.getStyle()
            .set("text-align", "center")
            .set("width", "100%");
        add(welcomeHeader);

        // commented out since we don't really have a usage for the chat in the welcome page

        // MessageList chatHistory = new MessageList();
        // chatHistory.setWidthFull();
        // chatHistory.setHeight("400px"); // you can adjust height as needed
        // add(chatHistory);

        // java.util.List<MessageListItem> messages = new java.util.ArrayList<>();

        // MessageInput chatbotInput = new MessageInput();
        // chatbotInput.setTooltipText("Write your message here...");
        // chatbotInput.setWidthFull();

        // chatbotInput.addSubmitListener(submitEvent -> {
        //     MessageListItem newMessage = new MessageListItem(
        //             submitEvent.getValue(), Instant.now(), "Milla Sting");
        //     newMessage.setUserColorIndex(3);
        //     List<MessageListItem> items = new ArrayList<>(chatHistory.getItems());
        //     items.add(newMessage);
        //     chatHistory.setItems(items);
        // });

        // add(chatbotInput);

        H2 extraSectionHeader = new H2("Oder nutze unsere weiteren Dienste.");
        extraSectionHeader.getStyle()
            .set("text-align", "center")
            .set("width", "100%");
        add(extraSectionHeader);

        IconGalleryComponent iconGallery = new IconGalleryComponent();
        // If you want a little spacing above/below, you can wrap it in another layout or set margins
        iconGallery.setWidthFull();
        add(iconGallery);
        

    }
}
