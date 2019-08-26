package com.cyecize.reporter.app.bindingModels;

import com.cyecize.reporter.common.dataAdapters.LocalDateAdapter;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.cyecize.reporter.common.ValidationConstants.*;
import static com.cyecize.reporter.common.ValidationMessageConstants.*;

public class CreateProjectBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @MaxLength(length = MAX_NAME_LENGTH, message = TEXT_TOO_LONG)
    private String projectName;

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(LocalDateAdapter.class)
    private LocalDateTime startDate;

    @ConvertedBy(LocalDateAdapter.class)
    private LocalDateTime endDate;

    public CreateProjectBindingModel() {

    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
}
