package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.contracts.ViewModel;
import com.cyecize.reporter.common.utils.Pair;

import java.util.List;

public class TaskDetailsViewModel implements ViewModel {

    private final Task task;

    private Pair<Long, Long> totalReportedTime;

    private final List<TaskViewModelAdvanced> subTasks;

    private final Project taskProject;

    public TaskDetailsViewModel(Task task, Pair<Long, Long> totalReportedTime, List<TaskViewModelAdvanced> subTasks, Project taskProject) {
        this.task = task;
        this.totalReportedTime = totalReportedTime;
        this.subTasks = subTasks;
        this.taskProject = taskProject;
    }

    public Task getTask() {
        return this.task;
    }

    public Pair<Long, Long> getTotalReportedTime() {
        return this.totalReportedTime;
    }

    public List<TaskViewModelAdvanced> getSubTasks() {
        return this.subTasks;
    }

    public Project getTaskProject() {
        return this.taskProject;
    }
}
