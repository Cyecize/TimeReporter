package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.common.contracts.ViewModel;

import java.util.List;

public class TaskDetailsViewModel implements ViewModel {

    private final TaskViewModelAdvanced task;

    private final List<TaskViewModelAdvanced> subTasks;

    private final Project taskProject;

    public TaskDetailsViewModel(TaskViewModelAdvanced task, List<TaskViewModelAdvanced> subTasks, Project taskProject) {
        this.task = task;
        this.subTasks = subTasks;
        this.taskProject = taskProject;
    }

    public TaskViewModelAdvanced getTask() {
        return this.task;
    }

    public List<TaskViewModelAdvanced> getSubTasks() {
        return this.subTasks;
    }

    public Project getTaskProject() {
        return this.taskProject;
    }
}
