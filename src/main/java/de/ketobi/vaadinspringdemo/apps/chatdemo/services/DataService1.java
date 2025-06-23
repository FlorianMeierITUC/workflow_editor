package de.ketobi.vaadinspringdemo.apps.chatdemo.services;

import de.ketobi.vaadinspringdemo.main.entities.ExtractImageResponse;
import de.ketobi.vaadinspringdemo.main.entities.ExtractTextResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service("OldDataService")
public class DataService {
    private final WebClient webClient;

    public DataService1(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl("http://10.0.1.236:5002").build();
    }

    public MultipartBodyBuilder getBuilder(byte[] byteArray, String filename, boolean isImage) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ByteArrayResource(byteArray) {
            @Override
            public String getFilename() {
                return filename;
            }
        }).contentType(isImage ? MediaType.IMAGE_JPEG : MediaType.APPLICATION_PDF);

        return builder;
    }

    public <T> Mono<T> extractData(byte[] byteArray, String filename, boolean isImage, String endpoint,
            Class<T> responseType) {

        MultipartBodyBuilder builder = getBuilder(byteArray, filename, isImage);

        return webClient
                .post()
                .uri(endpoint)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(responseType)
                .doOnError(e -> System.err.println("Error extracting document: " + e.getMessage()));
    }

    // ToDo: Refactor -> extractBytes
    public Mono<ExtractTextResponse> extractText(byte[] textBytes, String filename) {
        return extractData(textBytes, filename, false, "/extract_text", ExtractTextResponse.class);

    }

    public Mono<ExtractImageResponse> extractImage(byte[] imageBytes, String filename) {
        return extractData(imageBytes, filename, true, "/extract_image", ExtractImageResponse.class);
    }
}