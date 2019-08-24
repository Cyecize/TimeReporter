package com.cyecize.reporter.display.services;

import com.cyecize.reporter.display.enums.ConfigFile;

public interface FileSystemConfigLoader {

    void initFiles(String dir);

    void saveConfig(ConfigFile configFile, String jsonData);

    String getConfig(ConfigFile configFile);

    <T> T getParsedConfig(ConfigFile configFile, Class<T> destinationType);
}
