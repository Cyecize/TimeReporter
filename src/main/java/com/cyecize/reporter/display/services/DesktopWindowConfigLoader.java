package com.cyecize.reporter.display.services;

import com.cyecize.reporter.conn.models.DbCredentials;

public interface DesktopWindowConfigLoader {

    void saveDatabaseCredentials(DbCredentials credentials);

    boolean loadSavedDatabase(String sessionId);
}
