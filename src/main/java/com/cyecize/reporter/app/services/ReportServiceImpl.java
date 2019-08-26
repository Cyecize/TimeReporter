package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.repositories.ReportRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;

    @Autowired
    public ReportServiceImpl(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Report> findByReporter(User reporter) {
        return this.repository.findByReporter(reporter);
    }

    @Override
    public List<Report> findByReporter(User reporter, Task task) {
        return this.repository.findByReporter(reporter, task);
    }
}
