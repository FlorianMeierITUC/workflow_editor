package de.ketobi.vaadinspringdemo.main.entities;

import lombok.Data;

import java.util.List;

@Data
public class ProjectListResponse {
    private List<ProjectDetailsResponse> projects;

}
