package com.cyecize.reporter.users;

/**
 * Names are the same as @{@link com.cyecize.reporter.users.enums.RoleType}
 * <p>
 * This class is used as a helper to set role types in @{@link com.cyecize.summer.areas.security.annotations.PreAuthorize}
 * annotation as it is impossible to set them directly from enum.
 */
public class RoleConstants {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String ROLE_USER = "ROLE_USER";
}
