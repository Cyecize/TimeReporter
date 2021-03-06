package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.ReportBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    void report(ReportBindingModel bindingModel, User loggedInUser);

    boolean removeReport(Report report);

    Report findById(Long id);

    Pair<Long, Long> findTotalReportedTime(Project project);

    Pair<Long, Long> findTotalReportedTimeForReporter(Project project, User reporter);

    Pair<Long, Long> findTotalReportedTimeForTask(Task task);

    Pair<Long, Long> findTotalReportedTimeForTask(Task task, User reporter);

    Pair<Long, Long> calculateTotalReportedTime(List<Report> reports);

    List<Report> findByReporter(User reporter);

    List<Report> findByReporter(User reporter, LocalDateTime startDate, LocalDateTime endDate);

    //TODO: make use of this methods in task details.
    List<Report> findByReporter(User reporter, Task task);

    List<Report> findByReporterAndProject(User reporter, Project project);
}
