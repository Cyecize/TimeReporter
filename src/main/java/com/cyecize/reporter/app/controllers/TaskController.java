package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.bindingModels.EditTaskBindingModel;
import com.cyecize.reporter.app.dataAdapters.IdToProjectAdapter;
import com.cyecize.reporter.app.dataAdapters.IdToTaskAdapter;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.reporter.app.services.TaskService;
import com.cyecize.reporter.app.viewModels.TaskDetailsViewModel;
import com.cyecize.reporter.app.viewModels.TaskViewModel;
import com.cyecize.reporter.app.viewModels.TaskViewModelAdvanced;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
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
import com.cyecize.summer.common.annotations.routing.*;
import com.cyecize.summer.common.models.JsonResponse;
import com.cyecize.summer.common.models.Model;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

import static com.cyecize.summer.areas.security.enums.AuthorizationType.LOGGED_IN;

@Controller
@PreAuthorize(role = RoleConstants.ROLE_ADMIN)
@RequestMapping("/tasks")
public class TaskController extends BaseController {

    private final TaskService taskService;

    private final ProjectService projectService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService, UserService userService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createTaskGetAction(Principal principal) {
        return super.view(
                "tasks/create.twig",
                "projects",
                this.projectService.findByOwner(this.userService.findOneByUsername(principal.getUser().getUsername()), true)
        );
    }

    @PostMapping("/create")
    public ModelAndView createTaskPostAction(@Valid CreateTaskBindingModel bindingModel, BindingResult bindingResult,
                                             Principal principal, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect("create");
        }

        if (!this.isUserProjectOwner(principal.getUser(), bindingModel.getProject())) {
            return super.redirect("/");
        }

        this.taskService.createTask(bindingModel);

        return super.redirect("my");
    }

    @GetMapping("/project/{projId}")
    @PreAuthorize(LOGGED_IN)
    public JsonResponse getTasksForProjectAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("projId") Project project) {
        return new JsonResponse().addAttribute(
                "items",
                this.taskService.findAllByProject(project, true).stream()
                        .map(task -> this.modelMapper.map(task, TaskViewModel.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/my")
    @PreAuthorize(LOGGED_IN)
    public ModelAndView myTasksAction(Principal principal, @RequestParam("skipCompleted") boolean skipCompleted) {
        final User loggedInUser = this.userService.findOneByUsername(principal.getUser().getUsername());
        return super.view("tasks/my-tasks.twig", "tasks", this.taskService.findAllTasksForUser(loggedInUser, skipCompleted).stream()
                .map(t -> {
                    final TaskViewModelAdvanced viewModel = this.modelMapper.map(t, TaskViewModelAdvanced.class);
                    viewModel.setTotalReportedTime(this.taskService.findTotalReportedTimeForTask(t, loggedInUser));

                    return viewModel;
                }).collect(Collectors.toList())
        );
    }

    @GetMapping("/details/{taskId}")
    @PreAuthorize(LOGGED_IN)
    public ModelAndView taskDetailsAction(@ConvertedBy(IdToTaskAdapter.class) @PathVariable("taskId") Task task) {

        return super.view(
                "tasks/details.twig",
                new TaskDetailsViewModel(
                        task,
                        this.taskService.findTotalReportedTimeForTaskRecursive(task),
                        task.getSubTasks().stream().map(t -> {
                            final TaskViewModelAdvanced taskViewModelAdvanced = this.modelMapper.map(t, TaskViewModelAdvanced.class);
                            taskViewModelAdvanced.setTotalReportedTime(this.taskService.findTotalReportedTimeForTaskRecursive(t));

                            return taskViewModelAdvanced;
                        }).collect(Collectors.toList()),
                        task.getProject())
        );
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editTaskGetAction(@ConvertedBy(IdToTaskAdapter.class) @PathVariable("id") Task task, Principal principal, Model model) {
        if (!this.isUserProjectOwner(principal.getUser(), task.getProject())) {
            return super.redirect("/");
        }

        model.addAttribute("model", task);
        model.addAttribute("allProjectTasks", this.taskService.findAllByProject(task.getProject(), false));

        return super.view("tasks/edit.twig");
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editTaskPostAction(@ConvertedBy(IdToTaskAdapter.class) @PathVariable("id") Task task, Principal principal,
                                           @Valid EditTaskBindingModel bindingModel, BindingResult bindingResult) {
        if (!this.isUserProjectOwner(principal.getUser(), task.getProject())) {
            return super.redirect("/");
        }

        if (bindingResult.hasErrors()) {
            return super.redirect(String.format("edit/%d", task.getId()));
        }

        this.taskService.editTask(task, bindingModel);

        return super.redirect(String.format("details/%d", task.getId()));
    }

    private boolean isUserProjectOwner(UserDetails user, Project project) {
        return user.getUsername().equals(project.getOwner().getUsername());
    }
}
