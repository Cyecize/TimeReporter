package com.cyecize.reporter.users.dataAdapters;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

@Component
public class UsernameToUserAdapter implements DataAdapter<User> {

    private final UserService userService;

    public UsernameToUserAdapter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String handle = httpSoletRequest.getBodyParameters().get(paramName);

        if (handle == null) {
            return null;
        }

        return this.userService.findOneByUsername(handle);
    }
}
