package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.users.entities.User;

public class ProjectParticipantNode {

    private final User user;

    private final Long totalReportedHours;

    public ProjectParticipantNode(User user, Long totalReportedHours) {
        this.user = user;
        this.totalReportedHours = totalReportedHours;
    }

    public User getUser() {
        return this.user;
    }

    public Long getTotalReportedHours() {
        return this.totalReportedHours;
    }
}
