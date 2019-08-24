package com.cyecize.reporter.display.enums;

public enum ConfigFile {
    DATABASE_CREDENTIALS("dbSettings.json"), WINDOW_PREFERENCES("windowPreferences.json");

    private final String fileName;

    ConfigFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
