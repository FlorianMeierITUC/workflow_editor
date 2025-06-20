package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.main.services.*;
import de.ketobi.vaadinspringdemo.main.entities.CreateProjectRequest;
import de.ketobi.vaadinspringdemo.main.entities.ExtractImageResponse;
import de.ketobi.vaadinspringdemo.main.entities.ExtractTextResponse;
import de.ketobi.vaadinspringdemo.main.entities.IndexingResponse;
import de.ketobi.vaadinspringdemo.main.entities.ProjectDetailsResponse;
import de.ketobi.vaadinspringdemo.main.entities.ProjectListResponse;
import de.ketobi.vaadinspringdemo.main.entities.IndexDocumentRequest;
import de.ketobi.vaadinspringdemo.main.entities.IndexDocumentResponse;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AusschreibungService {

    private final ChatService chatService;
    private final IndexingService indexingService;
    private final DataService dataService;
    private final Mapper mapper;

    @Autowired
    public AusschreibungService(ChatService chatService, IndexingService indexingService, DataService dataService,
            Mapper mapper) {
        this.chatService = chatService;
        this.indexingService = indexingService;
        this.dataService = dataService;
        this.mapper = mapper;
    }

    public Mono<IndexingResponse> createProject(Ausschreibung ausschreibung) {
        CreateProjectRequest request = this.mapper.mapToCreateProjectRequest(ausschreibung);
        System.out.println("Creating project with request: " + request);

        return indexingService.createProject(request);

    }

    public Mono<ProjectListResponse> listProjects() {
        return this.listProjects("");
    }

    public Mono<ProjectListResponse> listProjects(String userId) {
        return indexingService.listProjects(userId);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(UUID uuid) {
        return indexingService.getProjectDetails(uuid.toString());
    }

    public Mono<ExtractTextResponse> extractAusschreibungText(byte[] fileBytes, String filename) {
        return dataService.extractText(fileBytes, filename);
    }

    public Mono<ExtractImageResponse> extractAusschreibungImage(byte[] fileBytes, String filename) {
        return dataService.extractImage(fileBytes, filename);
    }

    public Mono<IndexDocumentResponse> indexDocument(
        String text, Ausschreibung ausschreibung) {

        IndexDocumentRequest request = new IndexDocumentRequest();
        request.setText(text);
        request.setProject_uuid(ausschreibung.getUuid().toString()); // Or however you store UUID
        request.setDocument_type("ausschreibung"); // Adjust as needed
        request.setDocument_title(ausschreibung.getTitle());

        return indexingService.indexDocument(request);
    }


}
