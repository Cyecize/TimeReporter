package com.cyecize.reporter.users.services;

import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.Role;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.reporter.users.repositories.RoleRepository;
import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;
import com.cyecize.summer.common.annotations.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean hasRole(RoleType roleType, List<GrantedAuthority> roles) {
        String roleTypeName = roleType.name();
        return roles.stream().anyMatch(r -> r.getAuthority().equals(roleTypeName));
    }

    @Override
    public GrantedAuthority getHighestPriorityRole(List<GrantedAuthority> roles) {
        RoleType[] availableRoleTypes = RoleType.values(); //from highest to lowest

        //Index of role, role itself.
        Pair<Integer, GrantedAuthority> highestRoleType = new Pair<>(availableRoleTypes.length - 1, null);

        for (GrantedAuthority role : roles) {
            RoleType currentRole = RoleType.valueOf(role.getAuthority());

            for (int i = 0; i < availableRoleTypes.length; i++) {
                if (availableRoleTypes[i] == currentRole) {
                    if (highestRoleType.getKey() >= i) {
                        highestRoleType = new Pair<>(i, role);
                    }

                    break;
                }
            }

            //Stop the iteration if the highest priority role has been found.
            if (highestRoleType.getKey() < 1) {
                break;
            }
        }

        return highestRoleType.getValue();
    }

    @Override
    public void initDbValues() {
        for (RoleType roleType : RoleType.values()) {
            if (this.findOneByRoleType(roleType) == null) {
                Role role = new Role();
                role.setAuthority(roleType.name());
                this.roleRepository.persist(role);
            }
        }
    }

    @Override
    public Role findOneById(Long id) {
        return this.roleRepository.find(id);
    }

    @Override
    public Role findOneByRoleType(RoleType type) {
        return this.roleRepository.findOneByRoleType(type);
    }

    @Override
    public RoleType[] getDependingRoles(RoleType role) {
        RoleType[] roles = new RoleType[0];
        switch (role) {
            case ROLE_USER:
                roles = new RoleType[]{RoleType.ROLE_USER};
                break;
            case ROLE_ADMIN:
                roles = RoleType.values();
        }

        return roles;
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }
}
