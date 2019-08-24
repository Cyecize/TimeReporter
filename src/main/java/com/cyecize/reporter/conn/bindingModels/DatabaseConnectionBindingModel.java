package com.cyecize.reporter.conn.bindingModels;

import com.cyecize.reporter.conn.dataAdapters.StringToDbProviderAdapter;
import com.cyecize.reporter.conn.enums.DatabaseProvider;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.Min;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;

import static com.cyecize.reporter.common.ValidationMessageConstants.*;

public class DatabaseConnectionBindingModel {

    @NotNull(message = EMPTY_FIELD)
    @ConvertedBy(StringToDbProviderAdapter.class)
    private DatabaseProvider databaseProvider;

    @NotEmpty(message = INVALID_VALUE)
    private String host;

    @NotNull(message = EMPTY_FIELD)
    @Min(value = 0, message = INVALID_VALUE)
    private Integer port;

    @NotNull(message = EMPTY_FIELD)
    private String username;

    private String password;

    public DatabaseConnectionBindingModel() {

    }

    public DatabaseProvider getDatabaseProvider() {
        return this.databaseProvider;
    }

    public void setDatabaseProvider(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
}
