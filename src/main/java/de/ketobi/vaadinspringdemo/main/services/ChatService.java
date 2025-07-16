package de.ketobi.vaadinspringdemo.main.services;

import de.ketobi.vaadinspringdemo.main.entities.*;
import de.ketobi.vaadinspringdemo.main.utils.APIClientHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ChatService {

    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    @Autowired
    public ChatService(@Qualifier("chatWebClient") WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
        System.out.println("ChatService initialized with WebClient and APIClientHelper");
    }

    public Mono<ChatResponse> sendMessage(ChatRequest request) {
        return apiHelper.postJSON(webClient, "/chat", request, ChatResponse.class);
    }

    public Mono<RAGChatResponse> sendRAGMessage(RAGChatRequest request) {
        return apiHelper.postJSON(webClient, "/rag_chat", request, RAGChatResponse.class);
    }

    public Mono<OnepagerResponse> generateOnePager(OnepagerDocumentsResponse OnepagerDocuments) {
        return apiHelper.postJSON(webClient, "/generate_onepager_answers", OnepagerDocuments, OnepagerResponse.class);
    }

}
