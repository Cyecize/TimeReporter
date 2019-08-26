package com.cyecize.reporter.users.dataAdapters;

import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;

import java.util.Arrays;

@Component
public class StringToRoleTypeAdapter implements DataAdapter<RoleType> {
    @Override
    public RoleType resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String value = httpSoletRequest.getBodyParameters().get(paramName);

        if (value == null) {
            return null;
        }

        return Arrays.stream(RoleType.values())
                .filter(rt -> rt.name().equals(value))
                .findFirst().orElse(null);
    }
}
