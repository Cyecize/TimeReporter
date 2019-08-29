package com.cyecize.reporter.app.bindingModels;

import com.cyecize.reporter.app.dataAdapters.IdToTaskAdapter;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.dataAdapters.LocalDateAdapter;
import com.cyecize.reporter.common.dataAdapters.LocalDateTimeAdapter;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.Max;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.cyecize.reporter.common.ValidationMessageConstants.EMPTY_FIELD;
import static com.cyecize.reporter.common.ValidationMessageConstants.INVALID_VALUE;
import static com.cyecize.reporter.common.ValidationMessageConstants.TEXT_TOO_LONG;

public class ReportBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(IdToTaskAdapter.class)
    private Task task;

    @NotNull(message = EMPTY_FIELD)
    @Min(value = 0, message = INVALID_VALUE)
    private Integer hours;

    @NotNull(message = EMPTY_FIELD)
    @Min(value = 0, message = INVALID_VALUE)
    @Max(value = 59, message = INVALID_VALUE)
    private Integer minutes;

    @ConvertedBy(LocalDateTimeAdapter.class)
    private LocalDateTime dateOfReport;

    @MaxLength(length = 255, message = TEXT_TOO_LONG)
    private String comment;

    public ReportBindingModel() {

    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getHours() {
        return this.hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return this.minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public LocalDateTime getDateOfReport() {
        return this.dateOfReport;
    }

    public void setDateOfReport(LocalDateTime dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
