package de.ketobi.vaadinspringdemo.apps.ausschreibung.views.AusschreibungDetails;

import de.ketobi.vaadinspringdemo.main.entities.Message;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.services.AusschreibungService;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AusschreibungKIChat extends VerticalLayout {

    private List<Message> chatHistory = new ArrayList<>();

    public AusschreibungKIChat(Ausschreibung ausschreibung, AusschreibungService ausschreibungsService) {
        // Initialize the chat interface here
        add(new H1("ITUC Chatbot"));
        add(new H4(
                "Der Chatbot ist speziell auf die Anforderungen und Inhalte des Ausschreibungsprojekts XYZ zugeschnitten. Er dient als unterstützendes Werkzeug zur Kommunikation, Dokumentation und Navigation innerhalb des Projektrahmens."));

        MessageList chatHistory = new MessageList();
        chatHistory.setWidthFull();
        chatHistory.setHeight("400px"); // you can adjust height as needed
        add(chatHistory);

        // java.util.List<MessageListItem> messages = new java.util.ArrayList<>();

        MessageInput chatbotInput = new MessageInput();
        chatbotInput.setTooltipText("Write your message here...");
        chatbotInput.setWidthFull();

        chatbotInput.addSubmitListener(submitEvent -> {
            MessageListItem newMessage = new MessageListItem(
                    submitEvent.getValue(), Instant.now(), "User"); // TODO: replace logic
            newMessage.setUserColorIndex(3);
            List<MessageListItem> items = new ArrayList<>(chatHistory.getItems());
            items.add(newMessage);
            chatHistory.setItems(items);
            // FIXME: not sure how to use the Vaadin class for the api call
            this.chatHistory.add(new Message("user", submitEvent.getValue()));

            ausschreibungsService.chatWithDocuments(this.chatHistory, ausschreibung).subscribe(response -> {
                getUI().ifPresent(
                        ui -> ui.access(() -> {
                            MessageListItem responseMessage = new MessageListItem(
                                    response.getResponse(), Instant.now(), "assistant");
                            responseMessage.setUserColorIndex(2); // Set a different color for the assistant
                            List<MessageListItem> updatedItems = new ArrayList<>(chatHistory.getItems());
                            updatedItems.add(responseMessage);
                            chatHistory.setItems(updatedItems);
                            // chatbotInput.clear(); // Clear input after sending message
                            // chatHistory.scrollToEnd(); // Scroll to the latest message
                            this.chatHistory.add(new Message("assistant", response.getResponse()));
                        }

                        ));
            });

        });

        add(chatbotInput);

    }
}
