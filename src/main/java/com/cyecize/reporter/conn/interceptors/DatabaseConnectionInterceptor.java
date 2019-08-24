package com.cyecize.reporter.conn.interceptors;

import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.conn.DbConnectionConstants;
import com.cyecize.reporter.conn.annotations.DisableJpaCache;
import com.cyecize.reporter.conn.annotations.JdbcOnly;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.areas.routing.models.ActionMethod;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.extensions.InterceptorAdapter;
import com.cyecize.summer.common.models.Model;

/**
 * Checks if DbConnectionStorageService contains connection for the given sessionId and redirects to DB_CONNECT_ROUTE
 * if there is no connection.
 */
@Component
public class DatabaseConnectionInterceptor implements InterceptorAdapter {

    private final DbConnectionStorageService connectionStorageService;

    public DatabaseConnectionInterceptor(DbConnectionStorageService connectionStorageService) {
        this.connectionStorageService = connectionStorageService;
    }

    @Override
    public boolean preHandle(HttpSoletRequest request, HttpSoletResponse response, Object handler) {

        if (handler instanceof ActionMethod && !request.getRelativeRequestURL().equals(DbConnectionConstants.DB_CONNECT_ROUTE)) {
            ActionMethod actionMethod = (ActionMethod) handler;
            UserDbConnection dbConnection = this.connectionStorageService.getDbConnection(request.getSession().getId());

            //If there are not DB connections or there is only JDBC connection but the controller requires ORM connection.
            if (dbConnection == null || (dbConnection.getOrmConnection() == null && actionMethod.getControllerClass().getAnnotation(JdbcOnly.class) == null)) {
                response.sendRedirect(request.getContextPath() + DbConnectionConstants.DB_CONNECT_ROUTE);
                return false;
            }

            //Inject entity manager for the current session.
            if (dbConnection.getOrmConnection() != null) {
                if (actionMethod.getMethod().isAnnotationPresent(DisableJpaCache.class) || actionMethod.getControllerClass().isAnnotationPresent(DisableJpaCache.class)) {
                    dbConnection.getEntityManager().close();
                    dbConnection.setEntityManager(dbConnection.getOrmConnection().createEntityManager());
                }

                BaseRepository.currentEntityManager = dbConnection.getEntityManager();
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpSoletRequest request, HttpSoletResponse response, Object handler, Model model) {
        BaseRepository.currentEntityManager = null;
    }
}
