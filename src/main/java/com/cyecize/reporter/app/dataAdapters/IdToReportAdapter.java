package com.cyecize.reporter.app.dataAdapters;

import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Component;

@Component
public class IdToReportAdapter implements DataAdapter<Report> {

    private final ReportService reportService;

    @Autowired
    public IdToReportAdapter(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public Report resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String reportId = httpSoletRequest.getBodyParameters().get(paramName);

        try {
            return this.reportService.findById(Long.parseLong(reportId));
        } catch (Exception ignored) {
        }

        return null;
    }
}
