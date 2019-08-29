package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.models.ModelAndView;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(LOGGED_IN)
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView homeAction() {
        return super.view("main/home.twig");
    }
}
