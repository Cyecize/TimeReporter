package com.cyecize.reporter.users.services;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.reporter.users.repositories.UserRepository;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public void createUser(String username, String password, RoleType role) {
        this.createUser(username, password, this.roleService.getDependingRoles(role));
    }

    @Override
    public void createUser(String username, String password, RoleType... roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        user.addRole(this.roleService.findOneByRoleType(RoleType.ROLE_USER));
        for (RoleType role : roles) {
            user.addRole(this.roleService.findOneByRoleType(role));
        }

        this.userRepository.persist(user);
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        this.userRepository.merge(user);
    }

    @Override
    public void setEnabled(User user, boolean enabled) {
        user.setEnabled(enabled);
        this.userRepository.merge(user);
    }

    @Override
    public void updateRole(User user, RoleType[] roles) {
        user.setRoles(new ArrayList<>());

        for (RoleType role : roles) {
            user.addRole(this.roleService.findOneByRoleType(role));
        }

        this.userRepository.merge(user);
    }

    @Override
    public User findOneById(Long id) {
        return this.userRepository.find(id);
    }

    @Override
    public User findOneByUsername(String username) {
        return this.userRepository.findOneByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
