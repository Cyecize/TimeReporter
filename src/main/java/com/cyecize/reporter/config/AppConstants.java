package com.cyecize.reporter.config;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppConstants {
    public static final String APPLICATION_NAME = "Time Reporter";

    public static final int APPLICATION_VERSION = 1;

    public static final String JCEF_CACHE_DIR = System.getProperty("java.io.tmpdir") + File.separator + "TimeReporterBrowserCache" + File.separator;

    public static final LocalDateTime MIN_SUPPORTED_DATE = LocalDate.ofYearDay(1970, 1).atStartOfDay();
}
