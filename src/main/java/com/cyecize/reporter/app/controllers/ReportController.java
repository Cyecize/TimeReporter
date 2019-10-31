package com.cyecize.reporter.app.controllers;

import com.cyecize.http.HttpStatus;
import com.cyecize.reporter.app.bindingModels.ReportBindingModel;
import com.cyecize.reporter.app.dataAdapters.IdToReportAdapter;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.common.dataAdapters.LocalDateTimeAdapter;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.config.AppConstants;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.*;
import com.cyecize.summer.common.models.JsonResponse;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(LOGGED_IN)
@RequestMapping("/reports")
public class ReportController extends BaseController {

    private final ReportService reportService;

    private final UserService userService;

    @Autowired
    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @PostMapping("/report")
    public ModelAndView reportAction(@Valid ReportBindingModel bindingModel, RedirectAttributes redirectAttributes, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addAttribute("model", bindingModel);
            return super.redirect("/");
        }

        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());

        this.reportService.report(bindingModel, loggedInUser);

        return super.redirect("/");
    }

    @GetMapping("/remove/{id}")
    public JsonResponse removeReportAction(@ConvertedBy(IdToReportAdapter.class) @PathVariable("id") Report report, Principal principal) {
        if (!principal.getUser().getUsername().equals(report.getReporter().getUsername())) {
            return new JsonResponse().setStatusCode(HttpStatus.UNAUTHORIZED).addAttribute("message", "This report is not yours!");
        }

        if (this.reportService.removeReport(report)) {
            return new JsonResponse().addAttribute("message", "Report was removed!");
        }

        return new JsonResponse().addAttribute("message", "Report was not removed");
    }

    @GetMapping("/my")
    public ModelAndView myReportsAction(Principal principal, Model model,
                                        @ConvertedBy(LocalDateTimeAdapter.class) @RequestParam("startDate") LocalDateTime startDate,
                                        @ConvertedBy(LocalDateTimeAdapter.class) @RequestParam("endDate") LocalDateTime endDate) {

        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());

        if (startDate == null) {
            startDate = AppConstants.MIN_SUPPORTED_DATE;
        } else {
            model.addAttribute("startDate", startDate);
        }

        if (endDate == null) {
            endDate = LocalDate.now().atTime(LocalTime.MAX);
        } else {
            model.addAttribute("endDate", endDate);
        }

        final List<Report> byReporter = this.reportService.findByReporter(loggedInUser, startDate, endDate);
        final Pair<Long, Long> totalReportedTime = this.reportService.calculateTotalReportedTime(byReporter);

        model.addAttribute("reports", byReporter);
        model.addAttribute("totalReportedTime", totalReportedTime);

        return super.view("reports/list.twig");
    }
}
