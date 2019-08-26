package com.cyecize.reporter.users.repositories;

import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.users.entities.Role;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.summer.common.annotations.Service;

@Service
public class RoleRepository extends BaseRepository<Role, Long> {

    public Role findOneByRoleType(RoleType type) {
        return super.queryBuilderSingle((qb, roleRoot) -> qb.where(super.criteriaBuilder.equal(roleRoot.get("authority"), type.name())));
    }
}
