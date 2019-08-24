package com.cyecize.reporter.users.services;

import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.summer.common.annotations.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public void createUser(String adminUsername, String adminPassword, RoleType roleType) {
        System.out.println("created user - " + adminUsername);
    }
}
