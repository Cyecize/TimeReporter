package com.cyecize.reporter.users.bindingModels;

import com.cyecize.reporter.users.constraints.UniqueUsername;
import com.cyecize.reporter.users.dataAdapters.StringToRoleTypeAdapter;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.*;

import static com.cyecize.reporter.common.ValidationConstants.*;
import static com.cyecize.reporter.common.ValidationMessageConstants.*;

public class NewUserBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @RegEx(value = USERNAME_REGEX, message = INVALID_VALUE)
    @MaxLength(length = MAX_NAME_LENGTH, message = TEXT_TOO_LONG)
    @UniqueUsername(message = USERNAME_TAKEN)
    private String username;

    @NotNull(message = EMPTY_FIELD)
    @MinLength(length = MIN_PASSWORD_LENGTH, message = TEXT_TOO_SHORT)
    @MaxLength(length = MAX_VARCHAR_LENGTH, message = TEXT_TOO_LONG)
    private String password;

    @FieldMatch(fieldToMatch = "password", message = PASSWORDS_DO_NOT_MATCH)
    private String confirmPassword;

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(StringToRoleTypeAdapter.class)
    private RoleType role;

    public NewUserBindingModel() {

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public RoleType getRole() {
        return this.role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
