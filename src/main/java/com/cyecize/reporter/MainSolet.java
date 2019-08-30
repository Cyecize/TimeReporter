package com.cyecize.reporter;

import com.cyecize.broccolina.BroccolinaConstants;
import com.cyecize.javache.ConfigConstants;
import com.cyecize.javache.services.JavacheConfigService;
import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.display.services.DisplayService;
import com.cyecize.reporter.display.services.FileSystemConfigLoader;
import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerBootApplication;
import com.cyecize.summer.constants.IocConstants;

public class MainSolet extends DispatcherSolet {
    public MainSolet() {
        SummerBootApplication.run(this, (classes -> {
            super.dependencyContainer.getObject(EntityMappingService.class).init(classes);
        }));
    }

    @Override
    protected void onApplicationLoaded() {
        super.dependencyContainer.getObject(FileSystemConfigLoader.class).initFiles(super.getSoletConfig().getAttribute(IocConstants.SOLET_CFG_ASSETS_DIR) + "");
        super.dependencyContainer.getObject(DisplayService.class).loadUserPreferences();
        this.handleEmbeddedBrowser();
    }

    private void handleEmbeddedBrowser() {
        final JavacheConfigService javacheConfigService = (JavacheConfigService) super.getSoletConfig().getAttribute(BroccolinaConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY);

        int serverPort = javacheConfigService.getConfigParam(ConfigConstants.SERVER_PORT, int.class);
        if (!StartUp.isAppStartedFromEmbeddedServer) {
            serverPort = Integer.parseInt(javacheConfigService.getConfigParam(ConfigConstants.SERVER_STARTUP_ARGS, String[].class)[1]);
        }

        super.dependencyContainer.getObject(DisplayService.class).initialize(serverPort);
    }
}
