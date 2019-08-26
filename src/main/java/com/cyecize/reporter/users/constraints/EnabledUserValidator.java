package com.cyecize.reporter.users.constraints;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.areas.validation.interfaces.ConstraintValidator;
import com.cyecize.summer.common.annotations.Component;

@Component
public class EnabledUserValidator implements ConstraintValidator<EnabledUser, User> {

    @Override
    public boolean isValid(User user, Object o) {
        if (user == null) {
            return true;
        }

        return user.getEnabled();
    }
}
