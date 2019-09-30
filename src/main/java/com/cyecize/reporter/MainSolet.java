package com.cyecize.reporter;

import com.cyecize.broccolina.BroccolinaConstants;
import com.cyecize.javache.ConfigConstants;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.reporter.common.services.AppRunnerCommunicationService;
import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.localConfig.services.FileSystemConfigLoader;
import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerBootApplication;
import com.cyecize.summer.constants.IocConstants;

public class MainSolet extends DispatcherSolet {

    public MainSolet() {
        SummerBootApplication.run(this, classes -> super.dependencyContainer.getObject(EntityMappingService.class).init(classes));
    }

    @Override
    protected void onApplicationLoaded() {
        super.dependencyContainer.getObject(FileSystemConfigLoader.class).initFiles(super.getSoletConfig().getAttribute(IocConstants.SOLET_CFG_ASSETS_DIR) + "");
        this.connectToAppRunner();
    }

    /**
     * Creates a connection with the C# app that is used to run this app at runtime.
     * Checks if a second startup param has been passed and if it is, parses it to int and
     * used it as a port to communicate with the C# runner app.
     */
    private void connectToAppRunner() {
        final JavacheConfigService configService = (JavacheConfigService) super.getSoletConfig().getAttribute(BroccolinaConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY);

        String[] startupArgs = configService.getConfigParam(ConfigConstants.SERVER_STARTUP_ARGS, String[].class);

        if (startupArgs.length > 1) {
            final int callbackPort = Integer.parseInt(startupArgs[1]);
            System.out.println(String.format("Calling back to app runner on port %d", callbackPort));
            super.dependencyContainer.getObject(AppRunnerCommunicationService.class).initialize(callbackPort);
        }
    }
}
