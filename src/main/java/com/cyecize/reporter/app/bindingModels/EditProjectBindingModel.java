package com.cyecize.reporter.app.bindingModels;

import com.cyecize.reporter.common.dataAdapters.LocalDateAdapter;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.cyecize.reporter.common.ValidationConstants.MAX_NAME_LENGTH;
import static com.cyecize.reporter.common.ValidationMessageConstants.EMPTY_FIELD;
import static com.cyecize.reporter.common.ValidationMessageConstants.TEXT_TOO_LONG;

public class EditProjectBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @MaxLength(length = MAX_NAME_LENGTH, message = TEXT_TOO_LONG)
    private String projectName;

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(LocalDateAdapter.class)
    private LocalDateTime endDate;

    @NotNull(message = EMPTY_FIELD)
    private Boolean completed;

    public EditProjectBindingModel() {

    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
