package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.bindingModels.ReportBindingModel;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

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
}
