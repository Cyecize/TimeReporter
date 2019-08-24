package com.cyecize.reporter.conn.models;

import com.cyecize.reporter.common.utils.ObjectUtils;
import com.cyecize.reporter.conn.utils.SqlConnectionUtils;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class UserDbConnection {

    private DbCredentials credentials;

    private SqlConnectionUtils connectionUtils;

    private Connection jdbcConnection;

    private EntityManagerFactory ormConnection;

    private EntityManager entityManager;

    private long lastUpdatedTime;

    public UserDbConnection() {
        this.lastUpdatedTime = new Date().getTime();
    }

    public UserDbConnection(Connection jdbcConnection, DbCredentials dbCredentials) {
        this();
        this.jdbcConnection = jdbcConnection;
        this.credentials = dbCredentials;
        this.connectionUtils = dbCredentials.getDatabaseProvider().getConnectionUtils();
    }

    public void closeConnections() {
        try {
            if (this.jdbcConnection != null && !this.jdbcConnection.isClosed()) {
                this.jdbcConnection.close();
            }

            if (this.entityManager != null && this.entityManager.isOpen()) {
                this.entityManager.close();
            }

            if (this.ormConnection != null && this.ormConnection.isOpen()) {
                this.ormConnection.close();
            }

            this.jdbcConnection = null;
            this.ormConnection = null;
            this.entityManager = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DbCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(DbCredentials credentials) {
        this.credentials = credentials;
    }

    public Connection getJdbcConnection() {
        if (this.jdbcConnection == null && this.entityManager != null && this.entityManager.isOpen()) {
            return ((SessionImpl) this.entityManager.unwrap(Session.class)).connection();
        }

        return this.jdbcConnection;
    }

    public void setJdbcConnection(Connection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    public EntityManagerFactory getOrmConnection() {
        return ormConnection;
    }

    public void setOrmConnection(EntityManagerFactory ormConnection) {
        this.ormConnection = ormConnection;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public SqlConnectionUtils getConnectionUtils() {
        return connectionUtils;
    }

    public void setConnectionUtils(SqlConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserDbConnection)) {
            return super.equals(obj);
        }

        UserDbConnection otherConnection = (UserDbConnection) obj;

        return ObjectUtils.objectsEqual(this.credentials, otherConnection.getCredentials());
    }

    @Override
    public int hashCode() {
        return this.credentials.toString().hashCode();
    }
}
