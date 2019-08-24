package com.cyecize.reporter.config;

import java.io.File;

public class AppConstants {
    public static final String APPLICATION_NAME = "Time Reporter";

    public static final int APPLICATION_VERSION = 1;

    public static final String JCEF_CACHE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "TimeReporterBrowserCache" + File.separator;
}
