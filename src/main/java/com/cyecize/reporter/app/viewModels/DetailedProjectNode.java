package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;

import java.util.Collection;

public class DetailedProjectNode {

    private final Project project;

    private final Collection<Task> mainTasks;

    private final Long totalReportedHours;

    public DetailedProjectNode(Project project, Collection<Task> mainTasks, Long totalReportedTime) {
        this.project = project;
        this.mainTasks = mainTasks;
        this.totalReportedHours = totalReportedTime;
    }

    public Project getProject() {
        return this.project;
    }

    public Collection<Task> getMainTasks() {
        return this.mainTasks;
    }

    public Long getTotalReportedHours() {
        return this.totalReportedHours;
    }
}
