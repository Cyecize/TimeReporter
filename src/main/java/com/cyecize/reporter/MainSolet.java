package com.cyecize.reporter;

import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerBootApplication;
import com.cyecize.summer.areas.scanning.services.DependencyContainer;

public class MainSolet extends DispatcherSolet {
    public MainSolet() {
        SummerBootApplication.run(this, (classes -> {
            DependencyContainer dependencyContainer = SummerBootApplication.dependencyContainer;
//            dependencyContainer.getObject(EntityMappingService.class).init(classes);
//            new Thread(() -> dependencyContainer.getObject(DisplayService.class).initialize()).start();
        }));
    }

    @Override
    protected void onApplicationLoaded() {
        DependencyContainer dependencyContainer = SummerBootApplication.dependencyContainer;
    }
}
