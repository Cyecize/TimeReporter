package com.cyecize.reporter.conn.utils;

import com.cyecize.reporter.config.AppConstants;
import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.hibernate.dialect.MySQL57Dialect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.cyecize.reporter.conn.DbConnectionConstants.*;

public class MySqlConnectionUtils extends BaseSqlConnectionUtils {

    private static final Properties ORM_MYSQL_PROPERTIES = new Properties();

    static {
        ORM_MYSQL_PROPERTIES.putAll(COMMON_ORM_CONNECTION_PROPERTIES);
        ORM_MYSQL_PROPERTIES.setProperty("hibernate.dialect", MySQL57Dialect.class.getName());
        ORM_MYSQL_PROPERTIES.setProperty("hibernate.connection.driver_class", Driver.class.getName());
    }

    @Override
    public boolean dbNameExists(String dbName, Connection connection) {
        dbName = dbName.toLowerCase();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SHOW DATABASES")) {
            while (resultSet.next()) {
                if (resultSet.getString("Database").toLowerCase().equals(dbName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public List<String> getDatabases(Connection connection) {
        List<String> databaseNames = new ArrayList<>();

        try (Statement selectDbStatement = connection.createStatement()) {
            final String databaseNameAlias = "Database";

            /*
             * Select all database names as `Database` where each database has the unique for the warehouse app table.
             */
            String queryForGettingDbNames = String.format("SELECT s.SCHEMA_NAME AS `%s` FROM INFORMATION_SCHEMA.SCHEMATA AS s " +
                            "WHERE LENGTH((SELECT t.TABLE_NAME FROM INFORMATION_SCHEMA.TABLES AS t WHERE t.TABLE_SCHEMA = s.SCHEMA_NAME AND t.TABLE_NAME = '%s')) > 0",
                    databaseNameAlias, DB_UNIQUE_TABLE_NAME
            );

            try (ResultSet databaseNamesResultSet = selectDbStatement.executeQuery(queryForGettingDbNames)) {
                while (databaseNamesResultSet.next()) {
                    String dbName = databaseNamesResultSet.getString(databaseNameAlias);

                    // Check if the unique table contains column version and that version number matches the current app version.
                    String queryForCheckingDbVersion = String.format("SELECT * FROM `%s`.`%s` WHERE %s = %d LIMIT 1",
                            dbName, DB_UNIQUE_TABLE_NAME, DB_UNIQUE_TABLE_VERSION_COLUMN_NAME, AppConstants.APPLICATION_VERSION
                    );

                    try (Statement checkVersionStatement = connection.createStatement(); ResultSet uniqueTableResultSet = checkVersionStatement.executeQuery(queryForCheckingDbVersion)) {
                        if (uniqueTableResultSet.next()) {
                            databaseNames.add(dbName);
                        }
                    }
                }
            }

            return databaseNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Connection createConnection(DatabaseConnectionBindingModel connectionBindingModel) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUser(connectionBindingModel.getUsername());
        dataSource.setPassword(connectionBindingModel.getPassword());
        dataSource.setServerName(connectionBindingModel.getHost());
        dataSource.setPort(connectionBindingModel.getPort());
        dataSource.setUseSSL(false);

        return dataSource.getConnection();
    }

    @Override
    protected Properties getProviderSpecificProperties(DbCredentials credentials) {
        Properties properties = new Properties();
        properties.putAll(ORM_MYSQL_PROPERTIES);

        final String connectionString = String.format(
                "jdbc:mysql://%s:%d/%s?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false",
                credentials.getHost(),
                credentials.getPort(),
                credentials.getDatabaseName());

        properties.setProperty("hibernate.connection.url", connectionString);

        return properties;
    }
}
