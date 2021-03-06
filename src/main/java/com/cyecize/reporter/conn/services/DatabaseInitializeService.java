package com.cyecize.reporter.conn.services;

import com.cyecize.ioc.models.ServiceDetails;
import com.cyecize.reporter.conn.serviceModels.AdminUserServiceModel;

import javax.persistence.EntityManager;
import java.util.Collection;

public interface DatabaseInitializeService {

    void initDatabaseInitializingServices(Collection<ServiceDetails> servicesAndBeans);

    void initializeDatabase(EntityManager entityManager);

    void initializeFirstTime(EntityManager entityManager, AdminUserServiceModel adminUserServiceModel);
}
