package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.reporter.app.viewModels.HomeViewModel;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.models.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(LOGGED_IN)
public class HomeController extends BaseController {

    private final ReportService reportService;

    private final UserService userService;

    @Autowired
    public HomeController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView homeAction(Principal principal) {
        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());

        final List<Report> reportsFromToday = this.reportService.findByReporter(loggedInUser, LocalDate.now().atStartOfDay(), LocalDateTime.now());

        return super.view(
                "main/home.twig",
                new HomeViewModel(reportsFromToday, this.reportService.calculateTotalReportedTime(reportsFromToday))
        );
    }
}
