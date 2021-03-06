package com.cyecize.reporter.common.services;

import com.cyecize.reporter.common.sockets.ClientSocket;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.localConfig.services.DbCredentialsConfigLoader;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;

import java.net.URISyntaxException;

@Service
public class AppRunnerCommunicationServiceImpl implements AppRunnerCommunicationService {

    private static final String APP_STARTED_MSG = "app_started";

    private static final String TERMINATE_APP_MSG = "app_stop";

    private static final String APP_INITIALIZE_MSG = "app_initialize";

    private final DbCredentialsConfigLoader dbCredentialsConfigLoader;

    private final DbConnectionStorageService dbConnectionStorageService;

    private ClientSocket socket;

    @Autowired
    public AppRunnerCommunicationServiceImpl(DbCredentialsConfigLoader dbCredentialsConfigLoader, DbConnectionStorageService dbConnectionStorageService) {
        this.dbCredentialsConfigLoader = dbCredentialsConfigLoader;
        this.dbConnectionStorageService = dbConnectionStorageService;
    }

    @Override
    public void initialize(int port) {
        try {
            this.socket = new ClientSocket(port, this::onMessageReceived, this::onConnectionClosed, this::onError);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.notifyAppRunner();
    }

    private void onMessageReceived(String message) {
        final String[] tokens = message.split("\\s+");

        switch (tokens[0]) {
            case TERMINATE_APP_MSG:
                if (tokens.length > 1) {
                    this.saveDbCredentials(tokens[1]);
                }

                System.exit(0);
                break;
            case APP_INITIALIZE_MSG:
                if (tokens.length > 1) {
                    this.dbCredentialsConfigLoader.loadSavedDatabase(tokens[1]);
                }
                
                break;
        }
    }

    private void onConnectionClosed(int code, String reason, boolean remote) {
        System.exit(0);
    }

    private void onError(Exception ex) {
        ex.printStackTrace();
        System.exit(1);
    }

    private void saveDbCredentials(String sessionId) {
        if (sessionId == null) {
            return;
        }

        final UserDbConnection userDbConnection = this.dbConnectionStorageService.getDbConnection(sessionId);
        if (userDbConnection != null) {
            this.dbCredentialsConfigLoader.saveDatabaseCredentials(userDbConnection.getCredentials());
        }
    }

    private void notifyAppRunner() {
        while (!this.socket.isOpen()) {

        }

        this.socket.send(APP_STARTED_MSG);
    }
}
