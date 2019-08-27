package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;

@Service
public class ReportRepository extends BaseRepository<Report, Long> {

    public Long findTotalReportedMinutesForProject(Project project) {
        return super.execute(reportActionResult -> reportActionResult.set(
                super.entityManager.createQuery("SELECT SUM(r.reportedMinutes) FROM Report r WHERE r.project =:project", Long.class)
                        .setParameter("project", project)
                        .getSingleResult()
        ), Long.class).get();
    }

    public List<Report> findByReporter(User reporter) {
        return super.queryBuilderList((qb, reportRoot) -> qb.where(
                super.criteriaBuilder.equal(reportRoot.get("reporter"), reporter))
        );
    }

    public List<Report> findByReporter(User reporter, Task task) {
        return super.queryBuilderList((qb, reportRoot) -> qb.where(
                super.criteriaBuilder.equal(reportRoot.get("reporter"), reporter),
                super.criteriaBuilder.equal(reportRoot.get("task"), task)
        ));
    }
}
