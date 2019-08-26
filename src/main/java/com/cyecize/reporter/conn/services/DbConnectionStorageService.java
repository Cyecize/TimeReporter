package com.cyecize.reporter.conn.services;

import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.models.UserDbConnection;

import java.sql.Connection;

public interface DbConnectionStorageService {

    void closeAllConnections();

    void filterOldConnections();

    void initWithJDBC(Connection connection, DbCredentials dbCredentials, String sessionId);

    void initWithORM(DbCredentials dbCredentials, String sessionId);

    String getDatabaseName(String sessionId);

    UserDbConnection getDbConnection(String sessionId);
}
