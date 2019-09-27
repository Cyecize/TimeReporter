package com.cyecize.reporter.localConfig.services;

import com.cyecize.reporter.localConfig.enums.ConfigFile;

public interface FileSystemConfigLoader {

    void initFiles(String dir);

    void saveConfig(ConfigFile configFile, String jsonData);

    String getConfig(ConfigFile configFile);

    <T> T getParsedConfig(ConfigFile configFile, Class<T> destinationType);
}
