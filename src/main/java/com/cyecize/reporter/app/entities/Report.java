package com.cyecize.reporter.app.entities;

import com.cyecize.reporter.users.entities.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "reported_minutes", nullable = false)
    private Integer reportedMinutes;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date_of_report", nullable = false)
    private LocalDateTime dateOfReport;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "reporter_id", referencedColumnName = "id", nullable = false)
    private User reporter;

    @ManyToOne(targetEntity = Task.class)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private Task task;

    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    public Report() {
        this.setDateOfReport(LocalDateTime.now());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReportedMinutes() {
        return this.reportedMinutes;
    }

    public void setReportedMinutes(Integer reportedMinutes) {
        this.reportedMinutes = reportedMinutes;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateOfReport() {
        return this.dateOfReport;
    }

    public void setDateOfReport(LocalDateTime dateOfReport) {
        this.dateOfReport = dateOfReport;
    }

    public User getReporter() {
        return this.reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
