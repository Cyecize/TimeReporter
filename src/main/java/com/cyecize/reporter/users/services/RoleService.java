package com.cyecize.reporter.users.services;

import com.cyecize.reporter.common.contracts.DbInitable;
import com.cyecize.reporter.users.entities.Role;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;

import java.util.List;

public interface RoleService extends DbInitable {

    boolean hasRole(RoleType roleType, List<GrantedAuthority> roles);

    GrantedAuthority getHighestPriorityRole(List<GrantedAuthority> roles);

    Role findOneById(Long id);

    Role findOneByRoleType(RoleType type);

    RoleType[] getDependingRoles(RoleType mainRole);

    List<Role> findAll();
}
