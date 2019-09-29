package com.cyecize.reporter.localConfig.services;

import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.conn.utils.SqlConnectionUtils;
import com.cyecize.reporter.localConfig.enums.ConfigFile;
import com.cyecize.summer.common.annotations.Service;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

@Service
public class DbCredentialsConfigLoaderImpl implements DbCredentialsConfigLoader {

    private final DbConnectionStorageService connectionStorageService;

    private final FileSystemConfigLoader configLoader;

    private final EntityMappingService entityMappingService;

    private final ModelMapper modelMapper;

    private final Gson gson;

    public DbCredentialsConfigLoaderImpl(DbConnectionStorageService connectionStorageService, FileSystemConfigLoader configLoader,
                                         EntityMappingService entityMappingService, ModelMapper modelMapper, Gson gson) {
        this.connectionStorageService = connectionStorageService;
        this.configLoader = configLoader;
        this.entityMappingService = entityMappingService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void saveDatabaseCredentials(DbCredentials credentials) {
        this.configLoader.saveConfig(ConfigFile.DATABASE_CREDENTIALS, this.gson.toJson(credentials));
    }

    @Override
    public boolean loadSavedDatabase(String sessionId) {
        final DbCredentials credentials = this.configLoader.getParsedConfig(ConfigFile.DATABASE_CREDENTIALS, DbCredentials.class);

        if (credentials.getDatabaseProvider() != null) {
            final SqlConnectionUtils connectionUtils = credentials.getDatabaseProvider().getConnectionUtils();
            final DatabaseConnectionBindingModel bindingModel = this.modelMapper.map(credentials, DatabaseConnectionBindingModel.class);

            if (connectionUtils.testConnection(bindingModel) && connectionUtils.dbNameExists(credentials.getDatabaseName(), connectionUtils.getConnection(bindingModel))) {
                this.connectionStorageService.initWithORM(credentials, sessionId);
                connectionUtils.connectWithORM(this.connectionStorageService.getDbConnection(sessionId), credentials.getDatabaseName(), this.entityMappingService.getAllEntities());
                return true;
            }
        }

        return false;
    }
}
