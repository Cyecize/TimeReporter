package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.repositories.ReportRepository;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;

    @Autowired
    public ReportServiceImpl(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pair<Long, Long> findTotalReportedTime(Project project) {
        final Long minutes = Objects.requireNonNullElse(this.repository.findTotalReportedMinutesForProject(project), 0L);
        return new Pair<>(minutes / 60, minutes % 60);
    }

    @Override
    public Pair<Long, Long> findTotalReportedTimeForReporter(Project project, User reporter) {
        final Long minutes = Objects.requireNonNullElse(this.repository.findTotalReportedMinutesForProjectAndUser(project, reporter), 0L);
        return new Pair<>(minutes / 60, minutes % 60);
    }

    @Override
    public Pair<Long, Long> findTotalReportedTimeForTask(Task task) {
        final Long minutes = Objects.requireNonNullElse(this.repository.findTotalReportedMinutesForTask(task), 0L);
        return new Pair<>(minutes / 60, minutes % 60);
    }

    @Override
    public Pair<Long, Long> findTotalReportedTimeForTask(Task task, User reporter) {
        final Long minutes = Objects.requireNonNullElse(this.repository.findTotalReportedMinutesForTaskAndUser(task, reporter), 0L);
        return new Pair<>(minutes / 60, minutes % 60);
    }

    @Override
    public List<Report> findByReporter(User reporter) {
        return this.repository.findByReporter(reporter);
    }

    @Override
    public List<Report> findByReporter(User reporter, Task task) {
        return this.repository.findByReporter(reporter, task);
    }

    @Override
    public List<Report> findByReporterAndProject(User reporter, Project project) {
        return this.repository.findByReporterAndProject(reporter, project);
    }
}
