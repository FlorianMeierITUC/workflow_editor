package de.ketobi.vaadinspringdemo.main.services;

import de.ketobi.vaadinspringdemo.main.entities.*;
import de.ketobi.vaadinspringdemo.main.utils.APIClientHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("MainChatService")
public class ChatService {

    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    @Autowired
    public ChatService(WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
    }

    public Mono<ChatResponse> sendMessage(ChatRequest request) {
        return apiHelper.postJSON(webClient, "/chat", request, ChatResponse.class);
    }

}
