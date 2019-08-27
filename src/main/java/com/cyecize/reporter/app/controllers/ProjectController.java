package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.reporter.app.viewModels.ListProjectsViewModel;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(role = RoleConstants.ROLE_ADMIN)
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/my")
    @PreAuthorize(LOGGED_IN)
    public ModelAndView myProjectsAction(Principal principal) {
        return super.view(
                "projects/list.twig",
                new ListProjectsViewModel(
                        "My Projects",
                        this.projectService.findInvolved(this.userService.findOneByUsername(principal.getUser().getUsername()))
                )
        );
    }

    @GetMapping("/all")
    public ModelAndView allProjectsAction() {
        return super.view("projects/list.twig", new ListProjectsViewModel("All Projects", this.projectService.findAll()));
    }

    @GetMapping("/create")
    public ModelAndView createProjectGetAction() {
        return super.view("projects/create.twig");
    }

    @PostMapping("/create")
    public ModelAndView createProjectPostAction(Principal principal, @Valid CreateProjectBindingModel bindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect("create");
        }

        this.projectService.createProject(bindingModel, this.userService.findOneByUsername(principal.getUser().getUsername()));

        return super.redirect("all");
    }
}
