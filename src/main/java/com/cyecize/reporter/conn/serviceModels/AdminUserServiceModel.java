package com.cyecize.reporter.conn.serviceModels;

public class AdminUserServiceModel {

    private String adminUsername;

    private String adminPassword;

    public AdminUserServiceModel() {

    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
