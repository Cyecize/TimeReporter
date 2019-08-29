package com.cyecize.reporter.app.controllers;

import com.cyecize.http.HttpStatus;
import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.bindingModels.EditProjectBindingModel;
import com.cyecize.reporter.app.dataAdapters.IdToProjectAdapter;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.reporter.app.services.ReportService;
import com.cyecize.reporter.app.services.TaskService;
import com.cyecize.reporter.app.viewModels.*;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
import com.cyecize.reporter.users.dataAdapters.IdToUserAdapter;
import com.cyecize.reporter.users.dataAdapters.UsernameToUserAdapter;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.reporter.users.services.UserService;
import com.cyecize.summer.areas.security.annotations.PreAuthorize;
import com.cyecize.summer.areas.security.interfaces.UserDetails;
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
import com.cyecize.summer.common.models.JsonResponse;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;
import org.modelmapper.ModelMapper;

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

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, TaskService taskService, ReportService reportService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
        this.reportService = reportService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/my")
    @PreAuthorize(LOGGED_IN)
    public ModelAndView myProjectsAction(Principal principal) {
        return super.view(
                "projects/list.twig",
                new ListProjectsViewModel(
                        "My Projects",
                        //TODO skip completed AUTO
                        this.projectService.findInvolved(this.userService.findOneByUsername(principal.getUser().getUsername()), false)
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
                                .map(project -> new DetailedProjectNode(project, this.taskService.findMainTasks(project), this.reportService.findTotalReportedTime(project)))
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
    @PreAuthorize(LOGGED_IN)
    public ModelAndView supplierDetailsAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("id") Project project, Principal principal, Model model) {

        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());
        model.addAttribute("myReports", this.reportService.findByReporterAndProject(loggedInUser, project));
        model.addAttribute("myReportsTotalHours", this.reportService.findTotalReportedTimeForReporter(project, loggedInUser));

        model.addAttribute("participants", project.getParticipants().stream()
                .map(p -> new ProjectParticipantNode(p, this.reportService.findTotalReportedTimeForReporter(project, p)))
                .collect(Collectors.toList())
        );

        return super.view(
                "projects/details.twig",
                "viewModel",
                new DetailedProjectNode(project, this.taskService.findMainTasks(project), this.reportService.findTotalReportedTime(project))
        );
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editGetAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("id") Project project, Principal principal) {
        if (!this.isUserOwner(principal.getUser(), project)) {
            return super.redirect("/");
        }

        return super.view("projects/edit.twig", "model", project);
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editPostAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("id") Project project, Principal principal,
                                       @Valid EditProjectBindingModel bindingModel, BindingResult bindingResult) {
        if (!this.isUserOwner(principal.getUser(), project)) {
            return super.redirect("/");
        }

        if (bindingResult.hasErrors()) {
            return redirect("edit/" + project.getId());
        }

        this.projectService.editProject(project, bindingModel);

        return super.redirect("details/" + project.getId());
    }

    @PostMapping("/{projectId}/add/{username}")
    public JsonResponse addParticipantAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("projectId") Project project, Principal principal,
                                             @ConvertedBy(UsernameToUserAdapter.class) @PathVariable(value = "username", required = false) User participant) {
        if (!this.isUserOwner(principal.getUser(), project)) {
            return new JsonResponse().setStatusCode(HttpStatus.UNAUTHORIZED).addAttribute("message", "This is not your project!");
        }

        if (participant == null) {
            return new JsonResponse().addAttribute("message", "Invalid Username!");
        }

        this.projectService.addParticipantToProject(project, participant);

        return new JsonResponse().addAttribute("message", "Participant added!");
    }

    @PostMapping("/{projectId}/remove/{username}")
    public JsonResponse removeParticipantAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("projectId") Project project, Principal principal,
                                                @ConvertedBy(UsernameToUserAdapter.class) @PathVariable(value = "username", required = false) User participant) {
        if (!this.isUserOwner(principal.getUser(), project)) {
            return new JsonResponse().setStatusCode(HttpStatus.UNAUTHORIZED).addAttribute("message", "This is not your project!");
        }

        if (participant == null) {
            return new JsonResponse().addAttribute("message", "Invalid Username!");
        }

        this.projectService.removeParticipantFromProject(project, participant);

        return new JsonResponse().addAttribute("message", "Participant removed!");
    }

    @GetMapping("/involved/{userId}")
    @PreAuthorize(LOGGED_IN)
    public JsonResponse findInvolvedProjectsForUserAction(@ConvertedBy(IdToUserAdapter.class) @PathVariable("userId") User user) {
        return new JsonResponse().addAttribute(
                "items",
                //TODO skip completed AUTO
                this.projectService.findInvolved(user, false).stream()
                        .map(p -> this.modelMapper.map(p, ProjectViewModel.class))
                        .collect(Collectors.toList())
        );
    }

    private boolean isUserOwner(UserDetails user, Project project) {
        return user.getUsername().equals(project.getOwner().getUsername());
    }
}
