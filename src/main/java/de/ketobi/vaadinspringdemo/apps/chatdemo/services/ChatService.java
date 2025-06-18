package de.ketobi.vaadinspringdemo.apps.chatdemo.services;

import de.ketobi.vaadinspringdemo.main.entities.ChatRequest;
import de.ketobi.vaadinspringdemo.main.entities.ChatResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("OldChatService")
public class ChatService {
    private final WebClient webClient;

    public ChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5001").build();
    }

    public <TRequest, TResponse> Mono<TResponse> sendMessage(ChatRequest message, Class<TResponse> responseType) {
        return this.webClient
                .post()
                .uri("/chat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(message)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse
                                .bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody))))
                .bodyToMono(responseType);
    }
}
