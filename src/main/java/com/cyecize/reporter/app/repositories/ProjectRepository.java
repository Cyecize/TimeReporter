package com.cyecize.reporter.app.repositories;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.common.repositories.utils.NotNullPredicateList;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class ProjectRepository extends BaseRepository<Project, Long> {

    private static final String COMPLETED_FIELD_NAME = "completed";

    private static final String OWNER_FIELD_NAME = "owner";

    private static final String PARTICIPANTS_FIELD_NAME = "participants";

    public List<Project> findByOwner(User owner) {
        return super.queryBuilderList((qb, projectRoot) -> qb.where(
                super.criteriaBuilder.equal(projectRoot.get(OWNER_FIELD_NAME), owner)
        ));
    }

    public List<Project> findByParticipant(User involvedUser, boolean skipCompleted) {
        return super.queryBuilderList((qb, projectRoot) -> {
                    final ListJoin<Project, User> collabs = projectRoot.joinList(PARTICIPANTS_FIELD_NAME);

                    qb.distinct(true);
                    qb.where(new NotNullPredicateList(
                            this.setSkipCompletedFlag(projectRoot, skipCompleted),
                            super.criteriaBuilder.equal(collabs.get("id"), involvedUser.getId())
                    ).toArray());
                }
        );
    }

    public List<Project> findAll(boolean skipCompleted) {
        return super.queryBuilderList((qb, projectRoot) -> qb.where(new NotNullPredicateList(
                this.setSkipCompletedFlag(projectRoot, skipCompleted)
        ).toArray()));
    }

    private Predicate setSkipCompletedFlag(Path path, boolean skipDisabled) {
        if (!skipDisabled) {
            return null;
        }

        return super.criteriaBuilder.equal(path.get(COMPLETED_FIELD_NAME), false);
    }
}
