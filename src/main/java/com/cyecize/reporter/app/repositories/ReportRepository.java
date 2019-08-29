package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Service;

import javax.persistence.criteria.Join;
import java.util.List;

@Service
public class ReportRepository extends BaseRepository<Report, Long> {

    private static final String REPORTER_FIELD = "reporter";
    private static final String TASK_FIELD = "task";

    public Long findTotalReportedMinutesForProject(Project project) {
        return super.execute(reportActionResult -> reportActionResult.set(
                super.entityManager.createQuery("SELECT SUM(r.reportedMinutes) FROM Report r INNER JOIN r.task t WHERE t.project =:project", Long.class)
                        .setParameter("project", project)
                        .getSingleResult()
        ), Long.class).get();
    }

    public Long findTotalReportedMinutesForProjectAndUser(Project project, User reporter) {
        return super.execute(reportActionResult -> reportActionResult.set(
                super.entityManager.createQuery("SELECT SUM(r.reportedMinutes) FROM Report r INNER JOIN r.task t WHERE t.project =:project AND r.reporter = :reporter", Long.class)
                        .setParameter("project", project)
                        .setParameter("reporter", reporter)
                        .getSingleResult()
        ), Long.class).get();
    }

    public Long findTotalReportedMinutesForTask(Task task) {
        return super.execute(reportActionResult -> reportActionResult.set(
                super.entityManager.createQuery("SELECT SUM(r.reportedMinutes) FROM  Report r WHERE r.task =:task", Long.class)
                        .setParameter("task", task)
                        .getSingleResult()
        ), Long.class).get();
    }

    public Long findTotalReportedMinutesForTaskAndUser(Task task, User reporter) {
        return super.execute(actionResult -> actionResult.set(
                super.entityManager.createQuery("SELECT SUM (r.reportedMinutes) FROM Report r WHERE r.task =:task AND r.reporter =:reporter", Long.class)
                        .setParameter("task", task)
                        .setParameter("reporter", reporter)
                        .getSingleResult()
        ), Long.class).get();
    }

    public List<Report> findByReporter(User reporter) {
        return super.queryBuilderList((qb, reportRoot) -> qb.where(
                super.criteriaBuilder.equal(reportRoot.get(REPORTER_FIELD), reporter))
        );
    }

    public List<Report> findByReporter(User reporter, Task task) {
        return super.queryBuilderList((qb, reportRoot) -> qb.where(
                super.criteriaBuilder.equal(reportRoot.get(REPORTER_FIELD), reporter),
                super.criteriaBuilder.equal(reportRoot.get(TASK_FIELD), task)
        ));
    }

    public List<Report> findByReporterAndProject(User reporter, Project project) {
        return super.queryBuilderList((qb, reportRoot) -> {
            Join<Report, Task> taskJoin = reportRoot.join(TASK_FIELD);

            qb.where(
                    super.criteriaBuilder.equal(reportRoot.get(REPORTER_FIELD), reporter),
                    super.criteriaBuilder.equal(taskJoin.get("project"), project)
            );
        });
    }
}
