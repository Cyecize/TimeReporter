package com.cyecize.reporter.conn.dataAdapters;

import com.cyecize.reporter.conn.enums.DatabaseProvider;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

import java.util.Arrays;

@Component
public class StringToDbProviderAdapter implements DataAdapter<DatabaseProvider> {
    @Override
    public DatabaseProvider resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String value = httpSoletRequest.getBodyParameters().get(paramName);

        return Arrays.stream(DatabaseProvider.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }
}
