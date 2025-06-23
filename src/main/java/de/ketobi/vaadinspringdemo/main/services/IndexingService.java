package de.ketobi.vaadinspringdemo.main.services;

import de.ketobi.vaadinspringdemo.main.entities.*;
import de.ketobi.vaadinspringdemo.main.utils.APIClientHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class IndexingService {

    private final WebClient webClient;
    private final APIClientHelper apiHelper;

    @Autowired
    public IndexingService(@Qualifier("indexingWebClient") WebClient webClient, APIClientHelper apiHelper) {
        this.webClient = webClient;
        this.apiHelper = apiHelper;
    }

    public Mono<ProjectListResponse> listProjects(UUID userId) {
        return apiHelper.getJSON(webClient, "/list_projects?user=" + userId, ProjectListResponse.class);
    }

    public Mono<ProjectDetailsResponse> getProjectDetails(UUID uuid) {
        return apiHelper.getJSON(webClient, "/get_project_by_uuid?project_uuid=" + uuid, ProjectDetailsResponse.class);
    }

    public Mono<ProjectResponse> createProject(CreateProjectRequest request) {
        return apiHelper.postJSON(webClient, "/create_project", request, ProjectResponse.class);
    }

    public Mono<ProjectResponse> updateProject(UpdateProjectRequest request) {
        return apiHelper.postJSON(webClient, "/update_project", request, ProjectResponse.class);
    }

    public Mono<DocumentResponse> retrieveDocuments(RetrieveDocumentsRequest request) {
        return apiHelper.postJSON(webClient, "/retrieve_documents", request, DocumentResponse.class);

    }

    public Mono<IndexDocumentResponse> indexDocument(IndexDocumentRequest request) {
        return apiHelper.postJSON(
                webClient,
                "/index_document_with_metadata",
                request,
                IndexDocumentResponse.class);
    }

    public Mono<OnepagerDocumentsResponse> getOnepagerDocuments(UUID projectUuid) {
        RetrieveOnepagerDocuments request = new RetrieveOnepagerDocuments(projectUuid);
        return apiHelper.postJSON(webClient, "/retrieve_onepager_documents", request,
                OnepagerDocumentsResponse.class);
    }

}
