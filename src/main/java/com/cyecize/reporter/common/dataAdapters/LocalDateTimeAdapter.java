package com.cyecize.reporter.common.dataAdapters;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converts MM/dd/yyyy hh:mm AM/PM to LocalDateTime.
 * Supports hours, minutes, seconds.
 */
@Component
public class LocalDateTimeAdapter implements DataAdapter<LocalDateTime> {
    @Override
    public LocalDateTime resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

        try {
            String rawDate = httpSoletRequest.getBodyParameters().get(paramName);
            if (rawDate == null) {
                rawDate = httpSoletRequest.getQueryParameters().get(paramName);
            }

            return LocalDateTime.parse(rawDate, formatter);
        } catch (Exception ignored) {

        }

        return null;
    }
}
