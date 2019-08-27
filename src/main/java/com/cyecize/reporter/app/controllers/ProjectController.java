package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.dataAdapters.IdToProjectAdapter;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.reporter.app.services.TaskService;
import com.cyecize.reporter.app.viewModels.DetailedProjectNode;
import com.cyecize.reporter.app.viewModels.ListProjectsAdvancedViewModel;
import com.cyecize.reporter.app.viewModels.ListProjectsViewModel;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.GetMapping;
import com.cyecize.summer.common.annotations.routing.PathVariable;
import com.cyecize.summer.common.annotations.routing.PostMapping;
import com.cyecize.summer.common.annotations.routing.RequestMapping;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;

import java.util.stream.Collectors;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(role = RoleConstants.ROLE_ADMIN)
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    private final UserService userService;

    private final TaskService taskService;

    private final ReportService reportService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService, ReportService reportService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
        this.reportService = reportService;
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
        return super.view(
                "projects/list.twig",
                new ListProjectsAdvancedViewModel(
                        "All Projects",
                        this.projectService.findAll().stream()
                                .map(project -> new DetailedProjectNode(project, this.taskService.findMainTasks(project), this.reportService.findTotalReportedHours(project)))
                                .collect(Collectors.toList())
                )
        );
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

    @GetMapping("/details/{id}")
    public ModelAndView supplierDetailsAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("id") Project project, Principal principal, Model model) {

        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());
        model.addAttribute("myReports", this.reportService.findByReporterAndProject(loggedInUser, project));
        model.addAttribute("myReportsTotalHours", this.reportService.findTotalReportedHoursForReporter(project, loggedInUser));

        return super.view(
                "projects/details.twig",
                "viewModel",
                new DetailedProjectNode(project, this.taskService.findMainTasks(project), this.reportService.findTotalReportedHours(project))
        );
    }
}
