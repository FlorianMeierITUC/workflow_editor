package de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;
import de.ketobi.vaadinspringdemo.main.entities.*;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Mapper {
    public Ausschreibung mapToAusschreibung(ProjectDetailsResponse p) {
        Ausschreibung ausschreibung = new Ausschreibung();
        ausschreibung.setUuid(p.getUuid());
        ausschreibung.setTitle(p.getDisplayName());
        ausschreibung.setAusschreibungsNumber(p.getTenderId());
        ausschreibung.setITUCNumber(p.getItucId());
        ausschreibung.setDate(p.getDueDate());
        // FIXME: Mocked status. Currently the response from the endpoint does not
        // provide a status.
        ausschreibung.setStatus("In Bearbeitung"); // Default status, can be changed later);
        // Set other fields as necessary
        return ausschreibung;
    }

    public List<Ausschreibung> mapAll(List<ProjectDetailsResponse> projects) {
        return projects.stream()
                .map(this::mapToAusschreibung)
                .toList();
    }

    private void fillBaseRequestFields(CreateProjectRequest request, Ausschreibung ausschreibung) {
        request.setName(ausschreibung.getTitle());
        request.setItucId(ausschreibung.getITUCNumber());
        request.setTenderId(ausschreibung.getAusschreibungsNumber());
        request.setDueDate(ausschreibung.getDate());
        request.setDisplayName(ausschreibung.getTitle());
    }

    public CreateProjectRequest mapToCreateProjectRequest(Ausschreibung ausschreibung) {
        CreateProjectRequest request = new CreateProjectRequest();
        fillBaseRequestFields(request, ausschreibung);
        return request;
    }

    public UpdateProjectRequest mapToUpdateProjectRequest(Ausschreibung ausschreibung) {
        UpdateProjectRequest request = new UpdateProjectRequest();
        fillBaseRequestFields(request, ausschreibung);
        request.setProjectUuid(ausschreibung.getUuid());
        return request;
    }

    public DeleteProjectRequest mapToDeleteProjectRequest(Ausschreibung ausschreibung) {
        DeleteProjectRequest request = new DeleteProjectRequest();
        request.setProjectUuid(ausschreibung.getUuid());
        return request;
    }

    public RetrieveDocumentsRequest mapToRetrieveDocumentsRequest(Ausschreibung ausschreibung,
            List<Message> chatHistory) {
        RetrieveDocumentsRequest request = new RetrieveDocumentsRequest();
        Message message = chatHistory.get(chatHistory.size() - 1);
        System.out.println("Last message content: " + message);
        request.setProjectUuid(ausschreibung.getUuid());
        request.setQuestion(message.getContent());
        request.setKeywords("");

        return request;
    }

    public RAGChatRequest mapToRAGChatRequest(DocumentResponse documentResponse, List<Message> chatHistory) {
        RAGChatRequest request = new RAGChatRequest();
        request.setChatHistory(chatHistory);
        request.setContext(documentResponse.getCombinedText());

        return request;
    }

    public IndexDocumentRequest mapToIndexDocumentRequest(String text, String filename, Ausschreibung ausschreibung) {
        IndexDocumentRequest request = new IndexDocumentRequest();
        request.setText(text);
        request.setProjectUuid(ausschreibung.getUuid());
        request.setDocumentTitle(filename);
        request.setDocumentType("pdf"); //FIXME: Set the document type based on the actual file type
        return request;
    }

    public UpdateDocumentRequest mapToUpdateDocumentRequest(String text, String filename, Ausschreibung ausschreibung, UUID documentUuid ) {
        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setText(text);
        request.setProjectUuid(ausschreibung.getUuid());
        request.setDocumentTitle(filename);
        request.setDocumentType("pdf");//FIXME: Set the document type based on the actual file type
        request.setDocumentUuid(documentUuid); 
        return request;
    }

}
