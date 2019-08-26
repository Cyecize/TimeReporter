package com.cyecize.reporter.users.repositories;

import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Service;

@Service
public class UserRepository extends BaseRepository<User, Long> {

    public User findOneByUsername(String username) {
        return super.queryBuilderSingle((qb, userRoot) -> qb.where(super.criteriaBuilder.equal(userRoot.get("username"), username)));
    }
}