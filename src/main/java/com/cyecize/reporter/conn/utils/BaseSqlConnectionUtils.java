package com.cyecize.reporter.conn.utils;

import com.cyecize.reporter.conn.bindingModels.CreateDatabaseBindingModel;
import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.entities.ReporterUniqueEntity;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.models.PersistenceUnitInfoImpl;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.serviceModels.AdminUserServiceModel;
import com.cyecize.reporter.conn.services.DatabaseInitializeService;
import com.cyecize.summer.SummerBootApplication;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.spi.PersistenceUnitInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BaseSqlConnectionUtils implements SqlConnectionUtils {

    protected static final Properties COMMON_ORM_CONNECTION_PROPERTIES = new Properties();

    static {
        COMMON_ORM_CONNECTION_PROPERTIES.setProperty("hibernate.show_sql", "true");
        COMMON_ORM_CONNECTION_PROPERTIES.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    private final ModelMapper modelMapper;

    protected BaseSqlConnectionUtils() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public void connectWithORM(UserDbConnection userDbConnection, String dbName, Collection<Class<?>> mappedEntities) {
        DbCredentials credentials = userDbConnection.getCredentials();
        credentials.setDatabaseName(dbName);

        this.createORMConnection(userDbConnection, credentials, mappedEntities);

        SummerBootApplication.dependencyContainer.getService(DatabaseInitializeService.class).initializeDatabase(userDbConnection.getEntityManager());
    }

    @Override
    public void createORMConnection(UserDbConnection userDbConnection, CreateDatabaseBindingModel bindingModel, Collection<Class<?>> mappedEntities) {
        DbCredentials credentials = userDbConnection.getCredentials();
        credentials.setDatabaseName(bindingModel.getDatabaseName());

        this.createORMConnection(userDbConnection, credentials, mappedEntities);

        this.initNewDatabase(userDbConnection);

        SummerBootApplication.dependencyContainer.getService(DatabaseInitializeService.class)
                .initializeFirstTime(userDbConnection.getEntityManager(), this.modelMapper.map(bindingModel, AdminUserServiceModel.class));
    }

    @Override
    public boolean testConnection(DatabaseConnectionBindingModel connectionBindingModel) {
        try (Connection connection = this.createConnection(connectionBindingModel)) {
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    @Override
    public Connection getConnection(DatabaseConnectionBindingModel connectionBindingModel) {
        try {
            return this.createConnection(connectionBindingModel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Connection createConnection(DatabaseConnectionBindingModel connectionBindingModel) throws SQLException;

    protected abstract Properties getProviderSpecificProperties(DbCredentials credentials);

    private void createORMConnection(UserDbConnection userDbConnection, DbCredentials credentials, Collection<Class<?>> mappedEntities) {
        userDbConnection.closeConnections();

        final String persistenceUnitName = UUID.randomUUID().toString();

        Properties connectionProperties = this.getProviderSpecificProperties(credentials);
        connectionProperties.setProperty("persistence-unit", persistenceUnitName);
        connectionProperties.setProperty("hibernate.connection.username", credentials.getUsername());
        if (credentials.getPassword() != null) {
            connectionProperties.setProperty("hibernate.connection.password", credentials.getPassword());
        }

        List<String> mappedEntitiesNames = mappedEntities.stream().map(Class::getName).collect(Collectors.toList());
        PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfoImpl(persistenceUnitName, mappedEntitiesNames, connectionProperties);

        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, connectionProperties);

        userDbConnection.setOrmConnection(emf);
        userDbConnection.setEntityManager(emf.createEntityManager());

    }

    /**
     * Persists the unique entry that is used to determine if a DB is compatible with the app.
     *
     * @param dbConnection the connection model that contains all DataSource info.
     */
    private void initNewDatabase(UserDbConnection dbConnection) {
        EntityManager entityManager = dbConnection.getEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        ReporterUniqueEntity uniqueEntity = new ReporterUniqueEntity();
        entityManager.persist(uniqueEntity);
        entityManager.flush();

        transaction.commit();
    }
}
