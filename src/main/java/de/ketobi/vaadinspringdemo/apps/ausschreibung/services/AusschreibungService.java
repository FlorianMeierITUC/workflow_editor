package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.main.services.*;
import de.ketobi.vaadinspringdemo.main.entities.*;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return indexingService.createProject(request);

    }

    public Mono<IndexingResponse> updateProject(Ausschreibung ausschreibung) {
        UpdateProjectRequest reqest = this.mapper.mapToUpdateProjectRequest(ausschreibung);
        return indexingService.updateProject(reqest);


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

        //ToDo: Add mapper logic to convert Ausschreibung to IndexDocumentRequest
        IndexDocumentRequest request = this.mapper.mapToIndexDocumentRequest(text, ausschreibung);
        IndexDocumentRequest request = new IndexDocumentRequest();
        request.setText(text);
        request.setProject_uuid(ausschreibung.getUuid().toString()); // Or however you store UUID
        request.setDocument_type("ausschreibung"); // Adjust as needed
        request.setDocument_title(ausschreibung.getTitle());

        return indexingService.indexDocument(request);
    }

    public Mono<ProjectListResponse> listProjects() {
        // FIXME: For now, we use a fixed user ID
        return this.listProjects(UUID.fromString());
    }

    public Mono<ProjectListResponse> listProjects(UUID userId) {
        return indexingService.listProjects(userId);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(UUID uuid) {
        return indexingService.getProjectDetails(uuid);
    }

}
