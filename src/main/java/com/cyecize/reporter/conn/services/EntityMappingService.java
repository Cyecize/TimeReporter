package com.cyecize.reporter.conn.services;

import java.util.Collection;
import java.util.List;

public interface EntityMappingService {

    void init(Collection<Class<?>> scannedApplicationClasses);

    List<String> getEntityTableNames();

    List<Class<?>> getAllEntities();
}
