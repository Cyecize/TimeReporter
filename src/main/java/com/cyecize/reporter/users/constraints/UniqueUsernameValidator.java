package com.cyecize.reporter.users.constraints;

import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String usernameOrEmail, Object bindingModel) {
        if (usernameOrEmail == null) {
            return false;
        }

        return this.userService.findOneByUsername(usernameOrEmail) == null;
    }
}
