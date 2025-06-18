package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.main.services.ChatService;
import de.ketobi.vaadinspringdemo.main.services.IndexingService;
import de.ketobi.vaadinspringdemo.main.entities.CreateProjectRequest;
import de.ketobi.vaadinspringdemo.main.entities.IndexingResponse;
import de.ketobi.vaadinspringdemo.main.entities.ProjectDetailsResponse;
import de.ketobi.vaadinspringdemo.main.entities.ProjectListResponse;

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
    private final Mapper mapper;

    @Autowired
    public AusschreibungService(ChatService chatService, IndexingService indexingService, Mapper mapper) {
        this.chatService = chatService;
        this.indexingService = indexingService;
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

}
