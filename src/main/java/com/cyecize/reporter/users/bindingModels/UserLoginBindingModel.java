package com.cyecize.reporter.users.bindingModels;

import com.cyecize.reporter.users.constraints.EnabledUser;
import com.cyecize.reporter.users.constraints.ValidPassword;
import com.cyecize.reporter.users.dataAdapters.UsernameToUserAdapter;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import static com.cyecize.reporter.common.ValidationMessageConstants.*;


public class UserLoginBindingModel {

    @ConvertedBy(UsernameToUserAdapter.class)
    @NotNull(message = USERNAME_NOT_FOUND)
    @EnabledUser(message = USERNAME_NOT_FOUND)
    private User username;

    @ValidPassword(usernameField = "username", message = INVALID_PASSWORD)
    private String password;

    private String referrer;

    public UserLoginBindingModel() {

    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
