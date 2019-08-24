package com.cyecize.reporter.conn.utils;

import com.cyecize.reporter.conn.bindingModels.CreateDatabaseBindingModel;
import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.models.UserDbConnection;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public interface SqlConnectionUtils {

    void connectWithORM(UserDbConnection userDbConnection, String dbName, Collection<Class<?>> mappedEntities);

    void createORMConnection(UserDbConnection userDbConnection, CreateDatabaseBindingModel bindingModel, Collection<Class<?>> mappedEntities);

    boolean testConnection(DatabaseConnectionBindingModel connectionBindingModel);

    boolean dbNameExists(String dbName, Connection connection);

    Connection getConnection(DatabaseConnectionBindingModel connectionBindingModel);

    List<String> getDatabases(Connection connection);
}
