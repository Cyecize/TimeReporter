package com.cyecize.reporter.users.services;

import com.cyecize.reporter.users.enums.RoleType;

public interface UserService {

    void createUser(String adminUsername, String adminPassword, RoleType roleType);
}
