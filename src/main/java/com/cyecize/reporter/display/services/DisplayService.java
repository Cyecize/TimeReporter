package com.cyecize.reporter.display.services;

public interface DisplayService {

    void initialize(int port);

    void loadUserPreferences();

    void saveWindowPreferences();

    void runEmbeddedBrowser(String startingUrl, int port);
}
