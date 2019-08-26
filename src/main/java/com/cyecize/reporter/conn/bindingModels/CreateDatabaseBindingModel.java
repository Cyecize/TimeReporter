package com.cyecize.reporter.conn.bindingModels;

import com.cyecize.summer.areas.validation.constraints.*;

import static com.cyecize.reporter.common.ValidationConstants.*;
import static com.cyecize.reporter.common.ValidationMessageConstants.*;

public class CreateDatabaseBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @RegEx(value = DATABASE_NAME_REGEX, message = INVALID_VALUE)
    private String databaseName;

    @NotNull(message = EMPTY_FIELD)
    @RegEx(value = USERNAME_REGEX, message = INVALID_VALUE)
    @MaxLength(length = MAX_NAME_LENGTH, message = TEXT_TOO_LONG)
    private String adminUsername;

    @NotNull(message = EMPTY_FIELD)
    @MinLength(length = MIN_PASSWORD_LENGTH, message = TEXT_TOO_SHORT)
    @MaxLength(length = MAX_VARCHAR_LENGTH, message = TEXT_TOO_LONG)
    private String adminPassword;

    @FieldMatch(fieldToMatch = "adminPassword", message = PASSWORDS_DO_NOT_MATCH)
    private String passwordConfirm;

    public CreateDatabaseBindingModel() {

    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getAdminUsername() {
        return this.adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return this.adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getPasswordConfirm() {
        return this.passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
