package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.utils.APIClientHelper;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("NewChatService")
public class ChatService {

    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    public ChatService(WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
    }

    public Mono<ChatResponse> sendMessage(ChatRequest request) {
        return apiHelper.postJSON(webClient, "/chat", request, ChatResponse.class);
    }

}
