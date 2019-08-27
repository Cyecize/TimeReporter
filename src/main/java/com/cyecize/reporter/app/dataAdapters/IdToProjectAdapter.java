package com.cyecize.reporter.app.dataAdapters;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Component;

@Component
public class IdToProjectAdapter implements DataAdapter<Project> {

    private final ProjectService projectService;

    @Autowired
    public IdToProjectAdapter(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String projectId = httpSoletRequest.getBodyParameters().get(paramName);

        try {
            return this.projectService.findById(Long.parseLong(projectId));
        } catch (Exception ignored) {
        }

        return null;
    }
}
