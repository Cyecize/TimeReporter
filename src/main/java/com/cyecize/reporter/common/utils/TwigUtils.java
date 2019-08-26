package com.cyecize.reporter.common.utils;

import com.cyecize.http.HttpRequest;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;

import java.util.stream.Collectors;

@Service
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

    private UserDbConnection getConnection(HttpRequest request) {
        return this.connectionStorageService.getDbConnection(request.getSession().getId());
    }
}
