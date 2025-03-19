package de.ketobi.vaadinspringdemo.apps.chatdemo.services;

import de.ketobi.vaadinspringdemo.apps.chatdemo.dtos.ChatRequest;
import de.ketobi.vaadinspringdemo.apps.chatdemo.dtos.ChatResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ChatService {
    private final WebClient webClient;

    public ChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://10.0.1.236:5001").build();
    }
    
    public Mono<ChatResponse> sendMessage(ChatRequest message){
        return this.webClient
                .post()
                .uri("/chat")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(message), ChatRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(jsonString -> {
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        ChatResponse response = mapper.readValue(jsonString, ChatResponse.class);
                        return Mono.just(response);
                    } catch (Exception e){
                        return Mono.error(e);
                    }
                });
    }
}
