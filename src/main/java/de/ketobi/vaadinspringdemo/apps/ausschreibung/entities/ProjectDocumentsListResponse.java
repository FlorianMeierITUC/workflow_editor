package de.ketobi.vaadinspringdemo.apps.ausschreibung.entities;

import java.util.List;

public class ProjectDocumentsListResponse {
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
