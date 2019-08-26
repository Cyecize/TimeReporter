package com.cyecize.reporter.common.utils;

import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.areas.security.interfaces.GrantedAuthority;
import com.cyecize.summer.areas.template.annotations.TemplateService;
import com.cyecize.summer.common.annotations.Service;

import java.util.stream.Collectors;

@Service
@TemplateService(serviceNameInTemplate = "utils")
public class TwigUtils {
    public String listUserRoles(User user) {
        return user.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
    }
}
