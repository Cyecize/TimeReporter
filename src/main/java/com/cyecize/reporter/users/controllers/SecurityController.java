package com.cyecize.reporter.users.controllers;

import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.conn.annotations.DisableJpaCache;
import com.cyecize.reporter.users.bindingModels.UserLoginBindingModel;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.ANONYMOUS;

@Controller
@PreAuthorize(ANONYMOUS)
@DisableJpaCache
public class SecurityController extends BaseController {

    @GetMapping("/login")
    public ModelAndView loginGetAction(Model model, HttpSoletRequest request) {
        String referer = "/";
        if (request.getQueryParameters().containsKey("callback"))
            referer = request.getQueryParameters().get("callback");

        if (!model.hasAttribute("model")) {
            UserLoginBindingModel bindingModel = new UserLoginBindingModel();
            bindingModel.setReferrer(referer);
            model.addAttribute("model", bindingModel);
        }

        return super.view("security/login.twig");
    }

    @PostMapping("/login")
    public ModelAndView loginPost(@Valid UserLoginBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors())
            return super.redirect("/login");

        User user = bindingModel.getUsername();

        principal.setUser(user);

        return super.redirect(bindingModel.getReferrer() != null ? bindingModel.getReferrer() : "/");
    }
}
