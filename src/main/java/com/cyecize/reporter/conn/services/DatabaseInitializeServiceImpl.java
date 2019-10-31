package com.cyecize.reporter.conn.services;

import com.cyecize.ioc.models.ServiceDetails;
import com.cyecize.reporter.common.contracts.DbInitable;
import com.cyecize.reporter.common.repositories.BaseRepository;
import com.cyecize.reporter.conn.serviceModels.AdminUserServiceModel;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.common.annotations.Service;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseInitializeServiceImpl implements DatabaseInitializeService {

    private final UserService userService;

    private List<DbInitable> dbInitablesServices;

    public DatabaseInitializeServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public void init() {
        //this.initDatabaseInitializingServices(SummerBootApplication.dependencyContainer.getImplementations(DbInitable.class));
    }

    @Override
    public void initDatabaseInitializingServices(Collection<ServiceDetails> servicesAndBeans) {
        this.dbInitablesServices = servicesAndBeans.stream()
                .map(s -> (DbInitable) s.getProxyInstance())
                .collect(Collectors.toList());
    }

    @Override
    public void initializeDatabase(EntityManager entityManager) {
        final EntityManager oldEm = BaseRepository.currentEntityManager;
        BaseRepository.currentEntityManager = entityManager;

        for (DbInitable dbInitablesService : this.dbInitablesServices) {
            dbInitablesService.initDbValues();
        }

        BaseRepository.currentEntityManager = oldEm;
    }

    @Override
    public void initializeFirstTime(EntityManager entityManager, AdminUserServiceModel adminUserServiceModel) {
        this.initializeDatabase(entityManager);

        final EntityManager oldEm = BaseRepository.currentEntityManager;
        BaseRepository.currentEntityManager = entityManager;

        this.userService.createUser(adminUserServiceModel.getAdminUsername(), adminUserServiceModel.getAdminPassword(), RoleType.ROLE_ADMIN);

        BaseRepository.currentEntityManager = oldEm;
    }
}
