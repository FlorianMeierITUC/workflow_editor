package de.ketobi.vaadinspringdemo.main.services;

import de.ketobi.vaadinspringdemo.main.entities.*;
import de.ketobi.vaadinspringdemo.main.utils.APIClientHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class IndexingService {

    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    @Autowired
    public IndexingService(@Qualifier("indexingWebClient") WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
    }

    public Mono<ProjectListResponse> listProjects(String userId) {
        return apiHelper.getJSON(webClient, "/list_projects?user=" + userId, ProjectListResponse.class);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(String uuid) {
        return apiHelper.getJSON(webClient, "/get_project_details?uuid=" + uuid, ProjectDetailsResponse.class);
    }

    public Mono<IndexingResponse> createProject(CreateProjectRequest request) {
        return apiHelper.postJSON(webClient, "/create_project", request, IndexingResponse.class);
    }

    public Mono<IndexDocumentResponse> indexDocument(IndexDocumentRequest request) {
        return apiHelper.postJSON(
            webClient,
            "/index_document_with_metadata",
            request,
            IndexDocumentResponse.class
        );
    }

}
