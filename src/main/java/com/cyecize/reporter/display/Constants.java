package com.cyecize.reporter.display;

import com.cyecize.reporter.StartUp;
import com.cyecize.reporter.config.AppConstants;

import java.net.URL;

public class Constants {
    public static final String WINDOW_TITLE = AppConstants.APPLICATION_NAME;

    public static final URL FAVICON_RESOURCE_LOCATION = StartUp.class.getResource("../../../appicon.png");

    //Window Sizes
    public static final int WINDOW_MIN_WIDTH = 1100;

    public static final int WINDOW_MIN_HEIGHT = 600;
}
