package com.cyecize.reporter.localConfig.enums;

public enum ConfigFile {
    DATABASE_CREDENTIALS("dbSettings.json");

    private final String fileName;

    ConfigFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
