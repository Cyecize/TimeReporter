package com.cyecize.reporter;

import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.display.services.DisplayService;
import com.cyecize.reporter.display.services.FileSystemConfigLoader;
import com.cyecize.solet.SoletConfig;
import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerBootApplication;
import com.cyecize.summer.areas.scanning.services.DependencyContainer;
import com.cyecize.summer.constants.IocConstants;

public class MainSolet extends DispatcherSolet {
    public MainSolet() {
        SummerBootApplication.run(this, (classes -> {
            DependencyContainer dependencyContainer = SummerBootApplication.dependencyContainer;
            dependencyContainer.getObject(EntityMappingService.class).init(classes);
            new Thread(() -> dependencyContainer.getObject(DisplayService.class).initialize()).start();
        }));
    }

    @Override
    protected void onApplicationLoaded() {
        DependencyContainer dependencyContainer = SummerBootApplication.dependencyContainer;

        dependencyContainer.getObject(FileSystemConfigLoader.class).initFiles(dependencyContainer.getObject(SoletConfig.class).getAttribute(IocConstants.SOLET_CFG_ASSETS_DIR) + "");
        dependencyContainer.getObject(DisplayService.class).loadUserPreferences();
    }
}
