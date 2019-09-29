package com.cyecize.reporter.localConfig.services;

import com.cyecize.reporter.conn.models.DbCredentials;

public interface DbCredentialsConfigLoader {

    void saveDatabaseCredentials(DbCredentials credentials);

    boolean loadSavedDatabase(String sessionId);
}
