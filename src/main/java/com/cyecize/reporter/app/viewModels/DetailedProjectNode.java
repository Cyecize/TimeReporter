package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.utils.Pair;

import java.util.Collection;

public class DetailedProjectNode {

    private final Project project;

    private final Collection<Task> mainTasks;

    private final Pair<Long, Long> totalReportedTime;

    public DetailedProjectNode(Project project, Collection<Task> mainTasks, Pair<Long, Long> totalReportedTime) {
        this.project = project;
        this.mainTasks = mainTasks;
        this.totalReportedTime = totalReportedTime;
    }

    public Project getProject() {
        return this.project;
    }

    public Collection<Task> getMainTasks() {
        return this.mainTasks;
    }

    public Pair<Long, Long> getTotalReportedTime() {
        return this.totalReportedTime;
    }
}
