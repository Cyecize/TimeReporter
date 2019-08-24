package com.cyecize.reporter.display.services;

public interface DisplayService {

    void initialize();

    void loadUserPreferences();

    void saveWindowPreferences();

    void runEmbeddedBrowser(String startingUrl, int port);
}
