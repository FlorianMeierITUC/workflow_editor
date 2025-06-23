package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.main.services.*;
import de.ketobi.vaadinspringdemo.main.entities.*;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;
import de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.ArrayList;

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

    public Mono<ProjectResponse> createProject(Ausschreibung ausschreibung) {
        CreateProjectRequest request = this.mapper.mapToCreateProjectRequest(ausschreibung);
        return indexingService.createProject(request);
    }

    public Mono<ProjectResponse> updateProject(Ausschreibung ausschreibung) {
        UpdateProjectRequest reqest = this.mapper.mapToUpdateProjectRequest(ausschreibung);
        return indexingService.updateProject(reqest);
    }

    public Mono<ExtractTextResponse> extractAusschreibungText(byte[] fileBytes, String filename) {
        return dataService.extractText(fileBytes, filename);
    }

    public Mono<ExtractImageResponse> extractAusschreibungImage(byte[] fileBytes, String filename) {
        return dataService.extractImage(fileBytes, filename);
    }

    public Mono<IndexDocumentResponse> indexDocument(
            String text, String filename, Ausschreibung ausschreibung) {

        // ToDo: Add mapper logic to convert Ausschreibung to IndexDocumentRequest
        IndexDocumentRequest request = this.mapper.mapToIndexDocumentRequest(text, filename, ausschreibung);

        return indexingService.indexDocument(request);
    }

    public Mono<ProjectListResponse> listProjects() {
        // FIXME: For now, we use a fixed user ID
        return this.listProjects(UUID.fromString("e875950a-5417-4e1d-a05c-908a8d076da3"));
    }

    public Mono<ProjectListResponse> listProjects(UUID userId) {
        return indexingService.listProjects(userId);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(UUID uuid) {
        return indexingService.getProjectDetails(uuid);
    }

    public Mono<byte[]> getOnepager(UUID projectUuid) {
        return indexingService.getOnepagerDocuments(projectUuid)
                .flatMap(chatService::generateOnePager)
                .flatMap(dataService::getOnepager);
    }

}
