package com.cyecize.reporter.display.services;

import com.cyecize.reporter.display.enums.ConfigFile;
import com.cyecize.reporter.display.models.MainFrame;
import com.cyecize.reporter.display.models.WindowPreferences;
import com.cyecize.summer.common.annotations.Service;
import com.google.gson.Gson;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Service
public class DisplayServiceImpl implements DisplayService {

    private final FileSystemConfigLoader configLoader;

    private final Gson gson;

    private final MainFrame mainFrame;

    private WindowPreferences windowPreferences;

    public DisplayServiceImpl(FileSystemConfigLoader configLoader, Gson gson) {
        this.configLoader = configLoader;
        this.gson = gson;
        this.mainFrame = new MainFrame();
    }

    @Override
    public void initialize() {
        this.runEmbeddedBrowser("/", 8000); //TODO make port configurable
    }

    @Override
    public void loadUserPreferences() {
        this.windowPreferences = this.configLoader.getParsedConfig(ConfigFile.WINDOW_PREFERENCES, WindowPreferences.class);
        this.initWindowPreferences();
        this.mainFrame.setUserPreferences(this.windowPreferences);
    }

    @Override
    public void runEmbeddedBrowser(String startingUrl, int port) {
        boolean useOsr = false;
        String url = String.format("http://localhost:%d%s", port, startingUrl);
        this.mainFrame.initBrowser(url, useOsr);
    }

    @Override
    public void saveWindowPreferences() {
        windowPreferences.setExtendedState(this.mainFrame.getExtendedState());

        if (this.mainFrame.getExtendedState() != MainFrame.MAXIMIZED_BOTH) {
            windowPreferences.setWindowWidth(this.mainFrame.getWidth());
            windowPreferences.setWindowHeight(this.mainFrame.getHeight());
        }

        this.configLoader.saveConfig(ConfigFile.WINDOW_PREFERENCES, this.gson.toJson(this.windowPreferences));
    }

    private void initWindowPreferences() {
        this.mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveWindowPreferences();
            }
        });
    }
}
