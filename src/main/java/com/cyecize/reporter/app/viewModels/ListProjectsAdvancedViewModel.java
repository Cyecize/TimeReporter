package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.common.contracts.ViewModel;

import java.util.Collection;

public class ListProjectsAdvancedViewModel implements ViewModel {
    private final String type;

    private final String viewTitle;

    private final Collection<DetailedProjectNode> projects;

    public ListProjectsAdvancedViewModel(String viewTitle, Collection<DetailedProjectNode> projects) {
        this.type = "advanced";
        this.viewTitle = viewTitle;
        this.projects = projects;
    }

    public String getType() {
        return this.type;
    }

    public String getViewTitle() {
        return this.viewTitle;
    }

    public Collection<DetailedProjectNode> getProjects() {
        return this.projects;
    }
}
