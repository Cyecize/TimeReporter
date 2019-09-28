package com.cyecize.reporter.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppConstants {
    public static final int APPLICATION_VERSION = 1;

    public static final LocalDateTime MIN_SUPPORTED_DATE = LocalDate.ofYearDay(1970, 1).atStartOfDay();
}
