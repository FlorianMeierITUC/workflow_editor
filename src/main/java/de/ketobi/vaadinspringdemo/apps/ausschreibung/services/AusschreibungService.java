package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.Ausschreibung;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.repositories.AusschreibungRepository;
import de.ketobi.vaadinspringdemo.main.services.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AusschreibungService {

    private final ChatService chatService;

    @Autowired
    public AusschreibungService(@Qualifier("MainChatService") ChatService chatService) {
        this.chatService = chatService;
    }
}
