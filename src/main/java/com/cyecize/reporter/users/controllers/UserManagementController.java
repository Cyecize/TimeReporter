package com.cyecize.reporter.users.controllers;

import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
import com.cyecize.reporter.users.bindingModels.NewUserBindingModel;
import com.cyecize.reporter.users.enums.RoleType;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

@Controller
@RequestMapping("/users")
@PreAuthorize(role = RoleConstants.ROLE_ADMIN)
public class UserManagementController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public ModelAndView createUserGetAction() {
        return super.view("users/create.twig", "roles", RoleType.values());
    }

    @PostMapping("/create")
    public ModelAndView createUserPostAction(@Valid NewUserBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect("create");
        }

        this.userService.createUser(bindingModel.getUsername(), bindingModel.getPassword(), bindingModel.getRole());

        return super.redirect("/");//TODO redirect to a page with all users or something like that.
    }
}
