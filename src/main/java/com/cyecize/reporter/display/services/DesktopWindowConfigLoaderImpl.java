package com.cyecize.reporter.display.services;

import com.cyecize.summer.common.annotations.Service;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

@Service
public class DesktopWindowConfigLoaderImpl implements DesktopWindowConfigLoader {

//    private final DbConnectionStorageService connectionStorageService;
//
//    private final FileSystemConfigLoader configLoader;
//
//    private final DisplayService displayService;
//
//    private final EntityMappingService entityMappingService;
//
//    private final LocalLanguage localLanguage;
//
//    private final ModelMapper modelMapper;
//
//    private final Gson gson;
//
//    public DesktopWindowConfigLoaderImpl(DbConnectionStorageService connectionStorageService, FileSystemConfigLoader configLoader, DisplayService displayService, EntityMappingService entityMappingService, LocalLanguage localLanguage, ModelMapper modelMapper, Gson gson) {
//        this.connectionStorageService = connectionStorageService;
//        this.configLoader = configLoader;
//        this.displayService = displayService;
//        this.entityMappingService = entityMappingService;
//        this.localLanguage = localLanguage;
//        this.modelMapper = modelMapper;
//        this.gson = gson;
//    }

//    @Override
//    public void loadSavedDatabase() {
//        DbCredentials credentials = this.configLoader.getParsedConfig(ConfigFile.DATABASE_CREDENTIALS, DbCredentials.class);
//
//        if (credentials.getDatabaseProvider() != null) {
//            SqlConnectionUtils connectionUtils = credentials.getDatabaseProvider().getConnectionUtils();
//            DatabaseConnectionBindingModel bindingModel = this.modelMapper.map(credentials, DatabaseConnectionBindingModel.class);
//
//            if (connectionUtils.testConnection(bindingModel) && connectionUtils.dbNameExists(credentials.getDatabaseName(), connectionUtils.getConnection(bindingModel))) {
//                this.connectionStorageService.initWithORM(credentials, this.displayService.getWindowFrameSessionId());
//                connectionUtils.connectWithORM(this.connectionStorageService.getDbConnection(this.displayService.getWindowFrameSessionId()), credentials.getDatabaseName(), this.entityMappingService.getAllEntities());
//            }
//        }
//    }
//
//    @Override
//    public void loadSavedLanguage() {
//        LanguagePreferences languagePreferences = this.configLoader.getParsedConfig(ConfigFile.LANGUAGE_PREFERENCES, LanguagePreferences.class);
//        if (languagePreferences.getLocaleType() != null) {
//            this.localLanguage.updateLanguage(languagePreferences.getLocaleType());
//        }
//    }
}
