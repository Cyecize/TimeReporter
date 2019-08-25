package com.cyecize.reporter.conn.controllers;

import com.cyecize.http.HttpSession;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.conn.DbConnectionConstants;
import com.cyecize.reporter.conn.annotations.DisableJpaCache;
import com.cyecize.reporter.conn.annotations.JdbcOnly;
import com.cyecize.reporter.conn.bindingModels.CreateDatabaseBindingModel;
import com.cyecize.reporter.conn.bindingModels.DatabaseConnectionBindingModel;
import com.cyecize.reporter.conn.bindingModels.DatabaseSelectBindingModel;
import com.cyecize.reporter.conn.enums.DatabaseProvider;
import com.cyecize.reporter.conn.models.DbCredentials;
import com.cyecize.reporter.conn.models.UserDbConnection;
import com.cyecize.reporter.conn.services.DbConnectionStorageService;
import com.cyecize.reporter.conn.services.EntityMappingService;
import com.cyecize.reporter.conn.utils.SqlConnectionUtils;
import com.cyecize.reporter.display.services.DesktopWindowConfigLoader;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.enums.AuthorizationType;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.models.JsonResponse;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

import static com.cyecize.reporter.conn.DbConnectionConstants.DB_CONNECT_ROUTE;

@Controller
@PreAuthorize(AuthorizationType.ANONYMOUS)
@JdbcOnly
@DisableJpaCache
public class DatabaseConnectionController extends BaseController {

    private static final String DATABASE_SELECT_ROUTE = "/database/select";

    private static final String DATABASE_SELECT_ROUTE_AUTHENTICATED = "/database/select/authenticated";

    private static final String DATABASE_CREATE_ROUTE = "/database/create";

    private static final String LOGIN_ROUTE = "/login";

    private static final String DISABLE_JPA_CACHE_ROUTE = "/database/cache/clear";

    private final DbConnectionStorageService connectionStorageService;

    private final DesktopWindowConfigLoader windowConfigLoader;

    private final EntityMappingService entityMappingService;

    private final ModelMapper modelMapper;

    public DatabaseConnectionController(DbConnectionStorageService connectionStorageService, DesktopWindowConfigLoader windowConfigLoader,
                                        EntityMappingService entityMappingService, ModelMapper modelMapper) {
        this.connectionStorageService = connectionStorageService;
        this.windowConfigLoader = windowConfigLoader;
        this.entityMappingService = entityMappingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(DB_CONNECT_ROUTE)
    public ModelAndView establishDbConnectionGetAction(HttpSession session, Model model, HttpSoletRequest request) throws SQLException {

        if (request.getQueryParameters().containsKey(DbConnectionConstants.LOAD_STORED_DB_CREDENTIALS_PARAM_NAME)) {
            return this.handleStoredDbCredentials(request);
        }

        UserDbConnection dbConnection = this.connectionStorageService.getDbConnection(session.getId());
        if (dbConnection != null) {

            if (dbConnection.getJdbcConnection() == null || dbConnection.getJdbcConnection().isClosed()) {
                SqlConnectionUtils connectionUtils = dbConnection.getCredentials().getDatabaseProvider().getConnectionUtils();
                DatabaseConnectionBindingModel bindingModel = this.modelMapper.map(dbConnection.getCredentials(), DatabaseConnectionBindingModel.class);

                if (connectionUtils.testConnection(bindingModel)) {
                    this.connectionStorageService.initWithJDBC(connectionUtils.getConnection(bindingModel), dbConnection.getCredentials(), session.getId());
                    return super.redirect(DB_CONNECT_ROUTE);
                }
            }

            if (!model.hasAttribute("model")) {
                model.addAttribute("model", dbConnection.getCredentials());
            }

            model.addAttribute("isConnPresent", true);
        }

        return super.view("conn/db-connection.twig", "providers", DatabaseProvider.values());
    }

    @PostMapping(DB_CONNECT_ROUTE)
    public ModelAndView establishDbConnectionPostAction(@Valid DatabaseConnectionBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect(DB_CONNECT_ROUTE);
        }

        SqlConnectionUtils connectionUtils = bindingModel.getDatabaseProvider().getConnectionUtils();

        if (!connectionUtils.testConnection(bindingModel)) {
            bindingResult.addNewError(new FieldError("model", "databaseProvider", "invalidCredentials", bindingModel.getHost()));
            return super.redirect(DB_CONNECT_ROUTE);
        }

        this.connectionStorageService.initWithJDBC(connectionUtils.getConnection(bindingModel), this.modelMapper.map(bindingModel, DbCredentials.class), session.getId());
        return super.redirect(DATABASE_SELECT_ROUTE);
    }

    @GetMapping(DATABASE_SELECT_ROUTE)
    public ModelAndView selectDatabaseGetAction(HttpSoletRequest request) {
        UserDbConnection dbConnection = this.connectionStorageService.getDbConnection(request.getSession().getId());
        SqlConnectionUtils connectionUtils = dbConnection.getCredentials().getDatabaseProvider().getConnectionUtils();

        return super.view("security/db-selection.twig", "dbNames", connectionUtils.getDatabases(dbConnection.getJdbcConnection()));
    }

    @GetMapping(DATABASE_SELECT_ROUTE_AUTHENTICATED)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public ModelAndView selectDatabaseAuthenticatedAction(Principal principal) {
        principal.logout();
        return super.redirect(DATABASE_SELECT_ROUTE);
    }

    @PostMapping(DATABASE_SELECT_ROUTE)
    public ModelAndView selectDatabasePostAction(@Valid DatabaseSelectBindingModel bindingModel, BindingResult bindingResult, HttpSession session) {

        UserDbConnection dbConnection = this.connectionStorageService.getDbConnection(session.getId());
        SqlConnectionUtils connectionUtils = dbConnection.getConnectionUtils();

        if (bindingResult.hasErrors()) {
            return super.redirect(DATABASE_SELECT_ROUTE);
        }

        if (!connectionUtils.dbNameExists(bindingModel.getDatabaseName(), dbConnection.getJdbcConnection())) {
            bindingResult.addNewError(new FieldError("model", "databaseName", "invalidDatabase", bindingModel.getDatabaseName()));
            return super.redirect(DATABASE_SELECT_ROUTE);
        }

        connectionUtils.connectWithORM(dbConnection, bindingModel.getDatabaseName(), this.entityMappingService.getAllEntities());

        this.windowConfigLoader.saveDatabaseCredentials(dbConnection.getCredentials());

        return super.redirect(LOGIN_ROUTE);
    }

    @GetMapping(DATABASE_CREATE_ROUTE)
    public ModelAndView createDatabaseGetAction() {
        return super.view("security/db-create.twig");
    }

    @PostMapping(DATABASE_CREATE_ROUTE)
    public ModelAndView createDatabasePostAction(@Valid CreateDatabaseBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSoletRequest request) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect(DATABASE_CREATE_ROUTE);
        }

        UserDbConnection dbConnection = this.connectionStorageService.getDbConnection(request.getSession().getId());
        SqlConnectionUtils connectionUtils = dbConnection.getConnectionUtils();

        if (connectionUtils.dbNameExists(bindingModel.getDatabaseName(), dbConnection.getJdbcConnection())) {
            bindingResult.addNewError(new FieldError("model", "databaseName", "databaseNameInUse", bindingModel.getDatabaseName()));
            return super.redirect(DATABASE_CREATE_ROUTE);
        }

        connectionUtils.createORMConnection(dbConnection, bindingModel, this.entityMappingService.getAllEntities());

        this.windowConfigLoader.saveDatabaseCredentials(dbConnection.getCredentials());

        return super.redirect(LOGIN_ROUTE);
    }

    /**
     * In practice does nothing.
     * the @DisableJpaCache annotation, which is present on the
     * controller will clear the jpa cache.
     *
     * @return empty json response
     */
    @GetMapping(DISABLE_JPA_CACHE_ROUTE)
    @PreAuthorize(AuthorizationType.LOGGED_IN)
    public JsonResponse refreshDbCacheAction() {
        return new JsonResponse();
    }

    private ModelAndView handleStoredDbCredentials(HttpSoletRequest request) {
        if (this.windowConfigLoader.loadSavedDatabase(request.getSession().getId())) {
            return super.redirect(LOGIN_ROUTE);
        }

        return super.redirect(DB_CONNECT_ROUTE);
    }
}
