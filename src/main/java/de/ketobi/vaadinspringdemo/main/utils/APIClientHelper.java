package de.ketobi.vaadinspringdemo.main.utils;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.MultipartBodyBuilder;

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
                                                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                                                                "Error response: " + errorBody))))
                                .bodyToMono(responseType);
        }

        public <TRequest, TResponse> Mono<TResponse> postMultipartBody(WebClient webClient,
                        MultipartBodyBuilder builder,
                        String uri, Class<TResponse> responseType) {

                return webClient
                                .post()
                                .uri(uri)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .bodyValue(builder.build())
                                .retrieve()
                                .bodyToMono(responseType)
                                .doOnError(e -> System.err.println("Error extracting document: " + e.getMessage()));
        }

        public <TRequest, TResponse> Mono<TResponse> getJSON(WebClient webclient, String uri,
                        Class<TResponse> responseType) {
                return webclient
                                .get()
                                .uri(uri)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .retrieve()
                                .onStatus(
                                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                                res -> res.bodyToMono(String.class)
                                                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                                                                "Error response: " + errorBody))))
                                .bodyToMono(responseType);
        }

        public Mono<byte[]> getBinary(WebClient webclient, String uri) {
                return webclient
                                .get()
                                .uri(uri)
                                .accept(MediaType.APPLICATION_PDF)
                                .retrieve()
                                .onStatus(
                                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                                res -> res.bodyToMono(String.class)
                                                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                                                                "Error response: " + errorBody))))
                                .bodyToMono(byte[].class);
        }
}
