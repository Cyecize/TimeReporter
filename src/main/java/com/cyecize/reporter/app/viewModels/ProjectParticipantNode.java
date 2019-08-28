package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;

public class ProjectParticipantNode {

    private final User user;

    private final Pair<Long, Long> totalReportedTime;

    public ProjectParticipantNode(User user, Pair<Long, Long> totalReportedTime) {
        this.user = user;
        this.totalReportedTime = totalReportedTime;
    }

    public User getUser() {
        return this.user;
    }

    public Pair<Long, Long> getTotalReportedTime() {
        return this.totalReportedTime;
    }
}
