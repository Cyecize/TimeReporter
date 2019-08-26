package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;

@Service
public class ProjectRepository extends BaseRepository<Project, Long> {
    public List<Project> findByOwner(User owner) {
        return super.queryBuilderList((qb, projectRoot) -> qb.where(
           super.criteriaBuilder.equal(projectRoot.get("owner"), owner)
        ));
    }
}
