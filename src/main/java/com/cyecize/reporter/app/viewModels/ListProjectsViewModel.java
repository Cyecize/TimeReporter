package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.common.contracts.ViewModel;

import java.util.Collection;

public class ListProjectsViewModel implements ViewModel {

    private final String type;

    private final String viewTitle;

    private final Collection<Project> projects;

    public ListProjectsViewModel(String viewTitle, Collection<Project> projects) {
        this.type = "simple";
        this.viewTitle = viewTitle;
        this.projects = projects;
    }

    public String getViewTitle() {
        return this.viewTitle;
    }

    public Collection<Project> getProjects() {
        return this.projects;
    }
}
