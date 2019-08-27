package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;

@Service
public class TaskRepository extends BaseRepository<Task, Long> {

    public List<Task> findByNullParentAndProject(Project project) {
        return super.queryBuilderList((qb, taskRoot) -> qb.where(
                super.criteriaBuilder.isNull(taskRoot.get("parentTask")),
                super.criteriaBuilder.equal(taskRoot.get("project"), project)
        ));
    }
}
