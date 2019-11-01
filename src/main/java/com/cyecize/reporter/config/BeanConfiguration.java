package com.cyecize.reporter.config;

import com.cyecize.summer.areas.security.models.SecurityConfig;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.google.gson.Gson;
import org.hibernate.internal.SessionImpl;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import java.lang.reflect.Proxy;

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

    @Bean
    public EntityManager entityManager() {
        return null;
    }
}
