package com.cyecize.reporter.conn.models;

import com.cyecize.reporter.conn.enums.DatabaseProvider;

import static com.cyecize.reporter.common.utils.ObjectUtils.objectsEqual;

public class DbCredentials {

    private DatabaseProvider databaseProvider;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String databaseName;

    public DbCredentials() {

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

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DbCredentials) {
            DbCredentials otherCredentials = (DbCredentials) obj;

            return objectsEqual(this.databaseProvider, otherCredentials.databaseProvider) &&
                    objectsEqual(this.host, otherCredentials.host) &&
                    objectsEqual(this.port, otherCredentials.port) &&
                    objectsEqual(this.username, otherCredentials.username) &&
                    objectsEqual(this.password, otherCredentials.password) &&
                    objectsEqual(this.databaseName, otherCredentials.databaseName);
        }

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format("%s//%s:%d/%s?uid=%s&pwd=%s",
                this.databaseProvider.name(),
                this.host,
                this.port,
                this.databaseName,
                this.username,
                this.password);
    }
}
