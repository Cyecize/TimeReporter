package com.cyecize.reporter;

import com.cyecize.broccolina.BroccolinaConstants;
import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.reporter.common.contracts.DbInitable;
import com.cyecize.reporter.common.services.AppRunnerCommunicationService;
import com.cyecize.reporter.conn.services.DatabaseInitializeService;
import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.localConfig.services.FileSystemConfigLoader;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerBootApplication;

public class MainSolet extends DispatcherSolet {

    public MainSolet() {
        SummerBootApplication.run(this);
    }

    @Override
    protected void onApplicationLoaded() {
        super.dependencyContainer.getService(EntityMappingService.class).init(super.dependencyContainer.getAllScannedClasses());
        super.dependencyContainer.getService(FileSystemConfigLoader.class).initFiles(super.getSoletConfig().getAttribute(SoletConstants.SOLET_CONFIG_ASSETS_DIR) + "");
        super.dependencyContainer.getService(DatabaseInitializeService.class).initDatabaseInitializingServices(
                super.dependencyContainer.getImplementations(DbInitable.class)
        );

        this.connectToAppRunner();
    }

    /**
     * Creates a connection with the C# app that is used to run this app at runtime.
     * Checks if a second startup param has been passed and if it is, parses it to int and
     * used it as a port to communicate with the C# runner app.
     */
    private void connectToAppRunner() {
        final JavacheConfigService configService = (JavacheConfigService) super.getSoletConfig().getAttribute(BroccolinaConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY);

        final String[] startupArgs = configService.getConfigParam(JavacheConfigValue.SERVER_STARTUP_ARGS, String[].class);

        if (startupArgs.length > 1) {
            final int callbackPort = Integer.parseInt(startupArgs[1]);
            System.out.println(String.format("Calling back to app runner on port %d", callbackPort));
            super.dependencyContainer.getService(AppRunnerCommunicationService.class).initialize(callbackPort);
        }
    }
}
