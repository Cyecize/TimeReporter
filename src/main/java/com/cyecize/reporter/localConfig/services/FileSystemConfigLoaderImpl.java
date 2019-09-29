package com.cyecize.reporter.localConfig.services;

import com.cyecize.reporter.localConfig.enums.ConfigFile;
import com.cyecize.summer.common.annotations.Service;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemConfigLoaderImpl implements FileSystemConfigLoader {

    private final Gson gson;

    private String assetsDir;

    public FileSystemConfigLoaderImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void initFiles(String assetsDir) {
        this.setAssetsDir(assetsDir);

        for (ConfigFile configFile : ConfigFile.values()) {
            Path path = Paths.get(this.assetsDir + configFile.getFileName());

            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                try {
                    Files.write(path, "{}".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveConfig(ConfigFile configFile, String jsonData) {
        try {
            Files.write(Paths.get(this.assetsDir + configFile.getFileName()), jsonData.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getConfig(ConfigFile configFile) {
        try {
            return Files.readString(Paths.get(this.assetsDir + configFile.getFileName()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getParsedConfig(ConfigFile configFile, Class<T> destinationType) {
        return this.gson.fromJson(this.getConfig(configFile), destinationType);
    }

    private void setAssetsDir(String assetsDir) {
        this.assetsDir = assetsDir;
        if (!this.assetsDir.endsWith("\\") || !this.assetsDir.endsWith("/")) {
            this.assetsDir += File.separator;
        }
    }

    private String getAssetsDir() {
        return this.assetsDir;
    }
}
