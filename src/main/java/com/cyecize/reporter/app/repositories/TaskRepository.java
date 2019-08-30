package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.common.repositories.utils.NotNullPredicateList;
import com.cyecize.summer.common.annotations.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class TaskRepository extends BaseRepository<Task, Long> {

    private static final String PROJECT_FIELD = "project";

    private static final String COMPLETED_FIELD = "completed";

    private static final String PARENT_TASK = "parentTask";


    public List<Task> findByNullParentAndProject(Project project, boolean skipCompleted) {
        return super.queryBuilderList((qb, taskRoot) -> qb.where(new NotNullPredicateList(
                super.criteriaBuilder.isNull(taskRoot.get(PARENT_TASK)),
                super.criteriaBuilder.equal(taskRoot.get(PROJECT_FIELD), project),
                this.setSkipCompletedFlag(taskRoot, skipCompleted)
        ).toArray()));
    }

    public List<Task> findByProject(Project project, boolean skipCompleted) {
        return super.queryBuilderList((qb, taskRoot) -> qb.where(new NotNullPredicateList(
                super.criteriaBuilder.equal(taskRoot.get(PROJECT_FIELD), project),
                this.setSkipCompletedFlag(taskRoot, skipCompleted)
        ).toArray()));
    }

    private Predicate setSkipCompletedFlag(Path path, boolean skipDisabled) {
        if (!skipDisabled) {
            return null;
        }

        return super.criteriaBuilder.equal(path.get(COMPLETED_FIELD), false);
    }
}
