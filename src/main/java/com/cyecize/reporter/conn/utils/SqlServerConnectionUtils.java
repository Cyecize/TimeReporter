package com.cyecize.reporter.conn.utils;

import com.cyecize.reporter.config.AppConstants;
import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.models.SQLServerCustomDialect;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.cyecize.reporter.conn.DbConnectionConstants.*;

public class SqlServerConnectionUtils extends BaseSqlConnectionUtils {

    private static final Properties ORM_CONNECTION_SQL_SERVER_PROPERTIES = new Properties();

    static {
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.putAll(COMMON_ORM_CONNECTION_PROPERTIES);
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.put("hibernate.connection.driver_class", SQLServerDriver.class.getName());
        ORM_CONNECTION_SQL_SERVER_PROPERTIES.put("hibernate.dialect", SQLServerCustomDialect.class.getName());
    }

    @Override
    public boolean dbNameExists(String dbName, Connection connection) {
        return this.getAllDatabaseNames(connection).stream().anyMatch(d -> d.equalsIgnoreCase(dbName));
    }

    @Override
    public List<String> getDatabases(Connection connection) {
        List<String> databaseNames = new ArrayList<>();

        for (String dbName : this.getAllDatabaseNames(connection)) {
            try (Statement findUniqueTableStatement = connection.createStatement()) {

                //Select from current database all tables where table name equals the unique app table name.
                String queryForSearchingUniqueTable = String.format(
                        "SELECT [T].TABLE_NAME AS [TableName] FROM %s.INFORMATION_SCHEMA.TABLES AS [T] WHERE [T].TABLE_NAME = '%s'",
                        dbName, DB_UNIQUE_TABLE_NAME
                );

                try (ResultSet findUniqueTableResultSet = findUniqueTableStatement.executeQuery(queryForSearchingUniqueTable)) {
                    if (findUniqueTableResultSet.next()) {
                        try (Statement checkDbVersionStatement = connection.createStatement()) {
                            //Query for checking if the version number in the unique app table matches the current app version.
                            String checkWarehouseDbVersionQuery = String.format("SELECT * FROM %s.dbo.%s AS [metadata] WHERE [metadata].%s = %d",
                                    dbName, DB_UNIQUE_TABLE_NAME, DB_UNIQUE_TABLE_VERSION_COLUMN_NAME, AppConstants.APPLICATION_VERSION
                            );

                            try (ResultSet versionResultSet = checkDbVersionStatement.executeQuery(checkWarehouseDbVersionQuery)) {
                                if (versionResultSet.next()) {
                                    databaseNames.add(dbName);
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return databaseNames;
    }

    @Override
    protected Connection createConnection(DatabaseConnectionBindingModel connectionBindingModel) throws SQLException {
        SQLServerDataSource dataSource = new SQLServerDataSource();

        dataSource.setUser(connectionBindingModel.getUsername());
        dataSource.setPassword(connectionBindingModel.getPassword());
        dataSource.setServerName(connectionBindingModel.getHost());
        dataSource.setPortNumber(connectionBindingModel.getPort());
        dataSource.setLoginTimeout(2); //2s

        return dataSource.getConnection();
    }

    private List<String> getAllDatabaseNames(Connection connection) {
        final String databaseAlias = "Database";
        final String queryForGettingAllSchemas = String.format("SELECT [db].name AS [%s] FROM master.sys.databases AS [db]", databaseAlias);

        List<String> dbNames = new ArrayList<>();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(queryForGettingAllSchemas)) {
            while (resultSet.next()) {
                dbNames.add(resultSet.getString(databaseAlias));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dbNames;
    }

    @Override
    protected Properties getProviderSpecificProperties(DbCredentials credentials) {
        Properties properties = new Properties();
        properties.putAll(ORM_CONNECTION_SQL_SERVER_PROPERTIES);

        final String connectionString = String.format(
                "jdbc:sqlserver://%s:%d;DatabaseName=%s",
                credentials.getHost(),
                credentials.getPort(),
                credentials.getDatabaseName()
        );

        properties.put("hibernate.connection.url", connectionString);

        //TODO: Replace with MySQL createDatabaseIfNotExists equivalent.
        this.createDatabaseIfNotExists(credentials);

        return properties;
    }

    private void createDatabaseIfNotExists(DbCredentials credentials) {
        DatabaseConnectionBindingModel bindingModel = new DatabaseConnectionBindingModel() {{
            setHost(credentials.getHost());
            setPort(credentials.getPort());
            setUsername(credentials.getUsername());
            setPassword(credentials.getPassword());
            setDatabaseProvider(credentials.getDatabaseProvider());
        }};

        try (Connection connection = this.createConnection(bindingModel)) {

            //If there is no database, create one
            if (!this.dbNameExists(credentials.getDatabaseName(), connection)) {
                connection.prepareStatement("CREATE DATABASE " + credentials.getDatabaseName()).execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
