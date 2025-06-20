package de.ketobi.vaadinspringdemo.apps.ausschreibung.mapper;

import de.ketobi.vaadinspringdemo.apps.ausschreibung.entities.*;
import de.ketobi.vaadinspringdemo.main.entities.*;

import java.util.List;

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

}
