package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.users.entities.User;

import java.util.List;

public interface ReportService {

    List<Report> findByReporter(User reporter);

    List<Report> findByReporter(User reporter, Task task);
}