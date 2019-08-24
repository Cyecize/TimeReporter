package com.cyecize.reporter.conn.bindingModels;

import com.cyecize.reporter.common.ValidationMessageConstants;
import com.cyecize.summer.areas.validation.constraints.NotNull;

public class DatabaseSelectBindingModel {

    @NotNull(message = ValidationMessageConstants.EMPTY_FIELD)
    private String databaseName;

    public DatabaseSelectBindingModel() {

    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
