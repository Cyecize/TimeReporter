package com.cyecize.reporter.app.viewModels;

import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.common.contracts.ViewModel;
import com.cyecize.reporter.common.utils.Pair;

import java.util.List;

public class HomeViewModel implements ViewModel {

    private final List<Report> reportsFromToday;

    private final Pair<Long, Long> totalReportedTimeForToday;

    public HomeViewModel(List<Report> reportsFromToday, Pair<Long, Long> totalReportedTimeForToday) {
        this.reportsFromToday = reportsFromToday;
        this.totalReportedTimeForToday = totalReportedTimeForToday;
    }

    public List<Report> getReportsFromToday() {
        return this.reportsFromToday;
    }

    public Pair<Long, Long> getTotalReportedTimeForToday() {
        return this.totalReportedTimeForToday;
    }
}
