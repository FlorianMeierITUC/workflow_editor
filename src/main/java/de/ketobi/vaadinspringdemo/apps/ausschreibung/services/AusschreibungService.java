package de.ketobi.vaadinspringdemo.apps.ausschreibung.services;

import de.ketobi.vaadinspringdemo.main.services.*;
import de.ketobi.vaadinspringdemo.main.entities.CreateProjectRequest;
import de.ketobi.vaadinspringdemo.main.entities.UpdateProjectRequest;
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
    }

    public Mono<ProjectListResponse> listProjects() {
        // FIXME: For now, we use a random UUID for the user ID.
        return this.listProjects(UUID.randomUUID());
    }

    public Mono<ProjectListResponse> listProjects(UUID userId) {
        return indexingService.listProjects(userId);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(UUID uuid) {
        return indexingService.getProjectDetails(uuid);
    }

}
