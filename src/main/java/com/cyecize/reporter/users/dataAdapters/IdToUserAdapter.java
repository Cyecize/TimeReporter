package com.cyecize.reporter.users.dataAdapters;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;


@Component
public class IdToUserAdapter implements DataAdapter<User> {

    private final UserService userService;

    public IdToUserAdapter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String inputData = httpSoletRequest.getBodyParameters().get(paramName);

        if (inputData != null) {
            try {
                return this.userService.findOneById(Long.parseLong(inputData));
            } catch (NumberFormatException ignored) {
            }
        }

        return null;
    }
}
