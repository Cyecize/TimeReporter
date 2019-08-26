package com.cyecize.reporter.users.services;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.enums.RoleType;

import java.util.List;

public interface UserService {

    void createUser(String adminUsername, String adminPassword, RoleType roleType);

    void createUser(String username, String password, RoleType... roles);

    void changePassword(User user, String newPassword);

    void setEnabled(User user, boolean enabled);

    void updateRole(User user, RoleType[] roles);

    User findOneById(Long id);

    User findOneByUsername(String username);

    List<User> findAll();
}
