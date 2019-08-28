package com.cyecize.reporter.app.bindingModels;

import com.cyecize.reporter.app.dataAdapters.IdToProjectAdapter;
import com.cyecize.reporter.app.dataAdapters.IdToTaskAdapter;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.dataAdapters.LocalDateAdapter;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.cyecize.reporter.common.ValidationConstants.MAX_NAME_LENGTH;
import static com.cyecize.reporter.common.ValidationMessageConstants.EMPTY_FIELD;
import static com.cyecize.reporter.common.ValidationMessageConstants.TEXT_TOO_LONG;

public class CreateTaskBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @MaxLength(length = MAX_NAME_LENGTH, message = TEXT_TOO_LONG)
    private String taskName;

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(LocalDateAdapter.class)
    private LocalDateTime startDate;

    @ConvertedBy(LocalDateAdapter.class)
    private LocalDateTime endDate;

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(IdToProjectAdapter.class)
    private Project project;

    @ConvertedBy(IdToTaskAdapter.class)
    private Task parentTask;

    public CreateTaskBindingModel() {

    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getParentTask() {
        return this.parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }
}
