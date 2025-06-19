package de.ketobi.vaadinspringdemo.main.services;

import de.ketobi.vaadinspringdemo.main.entities.*;
import de.ketobi.vaadinspringdemo.main.utils.APIClientHelper;

import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.net.URLConnection;

@Service
public class DataService {
    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    @Autowired
    public DataService(@Qualifier("dataWebClient") WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
    }

    private MultipartBodyBuilder getBuilder(byte[] byteArray, String filename) {
        String mimeTypeStr = URLConnection.guessContentTypeFromName(filename);
        MediaType mediaType = (mimeTypeStr != null)
                ? MediaType.parseMediaType(mimeTypeStr)
                : MediaType.APPLICATION_OCTET_STREAM;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("file", new ByteArrayResource(byteArray) {
            @Override
            public String getFilename() {
                return filename;
            }
        }).contentType(mediaType);

        return builder;
    }

    public Mono<ExtractTextResponse> extractText(byte[] byteArray, String filename) {
        MultipartBodyBuilder builder = getBuilder(byteArray, filename);
        // ToDO: Endpoint likely to change
        return apiHelper.postMultipartBody(webClient, builder, "/extract_text", ExtractTextResponse.class);
    }

    public Mono<ExtractImageResponse> extractImage(byte[] byteArray, String filename) {
        MultipartBodyBuilder builder = getBuilder(byteArray, filename);
        return apiHelper.postMultipartBody(webClient, builder, "/extract_image", ExtractImageResponse.class);
    }
}
