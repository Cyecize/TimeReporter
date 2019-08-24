package com.cyecize.reporter.conn.services;

import com.cyecize.summer.common.annotations.Service;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityMappingServiceImpl implements EntityMappingService {

    private List<Class<?>> allEntities;

    private List<String> entityTableNames;

    public EntityMappingServiceImpl() {

    }

    @Override
    public void init(Collection<Class<?>> scannedApplicationClasses) {
        this.allEntities = scannedApplicationClasses.stream()
                .filter(cls -> this.isAnnotationPresent(Entity.class, cls))
                .collect(Collectors.toList());

        this.setEntityTableNames();
    }

    @Override
    public List<String> getEntityTableNames() {
        return this.entityTableNames;
    }

    @Override
    public List<Class<?>> getAllEntities() {
        return this.allEntities;
    }

    private void setEntityTableNames() {
        this.entityTableNames = this.allEntities.stream()
                .map(this::getTableName)
                .collect(Collectors.toList());
    }

    private boolean isAnnotationPresent(Class<? extends Annotation> annotation, Class<?> cls) {
        if (cls.isAnnotationPresent(annotation)) {
            return true;
        }

        if (cls.getSuperclass() != null) {
            return this.isAnnotationPresent(annotation, cls.getSuperclass());
        }

        return false;
    }

    private <T extends Annotation> T getAnnotation(Class<T> annotationType, Class<?> entity) {
        T annotation = entity.getAnnotation(annotationType);

        if (annotation != null) {
            return annotation;
        }

        if (entity.getSuperclass() != null) {
            return this.getAnnotation(annotationType, entity.getSuperclass());
        }

        return null;
    }

    private String getTableName(Class<?> entity) {
        if (this.isAnnotationPresent(Table.class, entity)) {
            return this.getAnnotation(Table.class, entity).name();
        }

        return entity.getSimpleName();
    }
}
