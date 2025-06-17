package de.ketobi.vaadinspringdemo.apps.ausschreibung.utils;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class APIClientHelper {

    public <TRequest, TResponse> Mono<TResponse> postJSON(WebClient webclient, String uri, TRequest message,
            Class<TResponse> responseType) {
        return webclient
                .post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(message)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        res -> res.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody))))
                .bodyToMono(responseType);
    }
}
