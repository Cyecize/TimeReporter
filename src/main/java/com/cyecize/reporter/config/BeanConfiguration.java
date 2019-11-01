package com.cyecize.reporter.config;

import com.cyecize.reporter.conn.services.DbConnectionStorageServiceImpl;
import com.cyecize.summer.areas.security.models.SecurityConfig;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.SessionFactory;
import com.cyecize.summer.common.enums.ServiceLifeSpan;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;

@BeanConfig
public class BeanConfiguration {

    @Bean
    public SecurityConfig getSecurityConfig() {
        return new SecurityConfig()
                .setLogoutURL("/logout")
                .setLogoutRedirectURL("/")
                .setLoginURL("/login")
                .setUnauthorizedURL("/unauthorized");
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    @Bean(lifespan = ServiceLifeSpan.SESSION)
    @SessionFactory(DbConnectionStorageServiceImpl.class)
    public EntityManager entityManager() {
        return null;
    }
}
