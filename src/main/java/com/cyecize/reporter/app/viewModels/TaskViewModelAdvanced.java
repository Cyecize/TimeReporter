package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.common.utils.Pair;

public class TaskViewModelAdvanced extends TaskViewModel {
    private Pair<Long, Long> totalReportedTime;

    public TaskViewModelAdvanced() {

    }

    public Pair<Long, Long> getTotalReportedTime() {
        return this.totalReportedTime;
    }

    public void setTotalReportedTime(Pair<Long, Long> totalReportedTime) {
        this.totalReportedTime = totalReportedTime;
    }
}
