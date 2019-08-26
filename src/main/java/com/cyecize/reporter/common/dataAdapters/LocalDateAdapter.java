package com.cyecize.reporter.common.dataAdapters;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts mm/dd/yyyy to LocalDateTime.
 * Does not support hours, minutes, seconds.
 */
@Component
public class LocalDateAdapter implements DataAdapter<LocalDateTime> {
    @Override
    public LocalDateTime resolve(String s, HttpSoletRequest httpSoletRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            String rawDate = httpSoletRequest.getBodyParameters().get(s);
            return LocalDate.parse(rawDate, formatter).atStartOfDay();
        } catch (Exception ignored) {
        }

        return null;
    }
}
