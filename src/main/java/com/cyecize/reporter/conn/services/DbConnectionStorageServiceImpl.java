package com.cyecize.reporter.conn.services;

import com.cyecize.reporter.conn.DbConnectionConstants;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.PostConstruct;
import com.cyecize.summer.common.annotations.Service;

import javax.swing.*;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@TemplateService(serviceNameInTemplate = "dbConnectionService")
public class DbConnectionStorageServiceImpl implements DbConnectionStorageService {

    private static final String NO_CURRENT_CONNECTION_MSG = "No current db connection for session";

    private Map<String, UserDbConnection> sessionConnectionMap;

    private UserDbConnection currentDbConnection;

    public DbConnectionStorageServiceImpl() {
        this.sessionConnectionMap = new HashMap<>();
    }

    @PostConstruct
    public void initCronJobs() {
        Timer timer = new Timer(5000, (e) -> this.filterOldConnections());

        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public void closeAllConnections() {
        this.sessionConnectionMap.forEach((sid, conn) -> conn.closeConnections());
    }

    @Override
    public void filterOldConnections() {
        long minConnectionTime = new Date().getTime() - DbConnectionConstants.USER_CONNECTION_MAX_INACTIVE_TIME_MILLIS;
        this.sessionConnectionMap = this.sessionConnectionMap.entrySet().stream()
                .filter((kvp) -> {
                    if (kvp.getValue().getLastUpdatedTime() > minConnectionTime) {
                        return true;
                    }

                    kvp.getValue().closeConnections();
                    return false;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void initWithJDBC(Connection connection, DbCredentials dbCredentials, String sessionId) {
        if (this.sessionConnectionMap.containsKey(sessionId)) {
            this.sessionConnectionMap.get(sessionId).closeConnections();
        }

        this.sessionConnectionMap.put(sessionId, new UserDbConnection(connection, dbCredentials));
    }

    @Override
    public void initWithORM(DbCredentials dbCredentials, String sessionId) {
        this.initWithJDBC(null, dbCredentials, sessionId);
    }

    @Override
    public String getDatabaseName(String sessionId) {
        if (!this.sessionConnectionMap.containsKey(sessionId)) {
            return null;
        }

        return this.sessionConnectionMap.get(sessionId).getCredentials().getDatabaseName();
    }

    @Override
    public UserDbConnection getCurrentDbConnection() throws NullPointerException {
        if (this.currentDbConnection == null) {
            throw new NullPointerException(NO_CURRENT_CONNECTION_MSG);
        }

        return this.currentDbConnection;
    }

    @Override
    public UserDbConnection getDbConnection(String sessionId) {
        if (this.sessionConnectionMap.containsKey(sessionId)) {
            UserDbConnection dbConnection = this.sessionConnectionMap.get(sessionId);
            dbConnection.setLastUpdatedTime(new Date().getTime());
            this.currentDbConnection = dbConnection;

            return dbConnection;
        }

        return null;
    }
}
