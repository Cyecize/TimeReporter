package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.common.contracts.ViewModel;

import java.util.List;

public class TaskDetailsViewModel implements ViewModel {

    private final TaskViewModelAdvanced task;

    private final List<TaskViewModelAdvanced> subTasks;

    public TaskDetailsViewModel(TaskViewModelAdvanced task, List<TaskViewModelAdvanced> subTasks) {
        this.task = task;
        this.subTasks = subTasks;
    }

    public TaskViewModelAdvanced getTask() {
        return this.task;
    }

    public List<TaskViewModelAdvanced> getSubTasks() {
        return this.subTasks;
    }
}
