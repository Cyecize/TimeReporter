package com.cyecize.reporter.common.utils;

import com.cyecize.http.HttpRequest;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@TemplateService(serviceNameInTemplate = "utils")
public class TwigUtils {

    private final DbConnectionStorageService connectionStorageService;

    @Autowired
    public TwigUtils(DbConnectionStorageService connectionStorageService) {
        this.connectionStorageService = connectionStorageService;
    }

    public String listUserRoles(User user) {
        return user.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
    }

    public boolean hasJdbcConnection(HttpRequest request) {
        final UserDbConnection dbConnection = this.getConnection(request);

        return dbConnection != null && dbConnection.getJdbcConnection() != null;
    }

    public boolean hasOrmConnection(HttpRequest request) {
        final UserDbConnection dbConnection = this.getConnection(request);

        return dbConnection != null && dbConnection.getOrmConnection() != null;
    }

    public String getDatabaseName(HttpRequest request) {
        if (!this.hasOrmConnection(request)) return null;
        return this.getConnection(request).getCredentials().getDatabaseName();
    }

    public String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String formatDateForJs(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String formatDateTimeForJs(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public long millisFromDate(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public long calculateDaysDiff(LocalDateTime start, LocalDateTime end) {
        return Math.abs(this.millisFromDate(start) - this.millisFromDate(end)) / 1000 / 60 / 60 / 24;
    }

    public String formatReportedTime(Pair<Long, Long> hourMinutePair) {
        return String.format("%d hours and %d minutes", hourMinutePair.getKey(), hourMinutePair.getValue());
    }

    public String formatReportedTimeFromMinutes(int minutes) {
        return this.formatReportedTime(new Pair<>(minutes / 60L, minutes % 60L));
    }

    public String sortReportedTime(Pair<Long, Long> hourMinutePair) {
        return String.valueOf(hourMinutePair.getKey() * 60 + hourMinutePair.getValue());
    }

    private UserDbConnection getConnection(HttpRequest request) {
        return this.connectionStorageService.getDbConnection(request.getSession().getId());
    }
}
