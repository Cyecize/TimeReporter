package com.cyecize.reporter.app.controllers;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.dataAdapters.IdToProjectAdapter;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.services.ProjectService;
import com.cyecize.reporter.app.services.TaskService;
import com.cyecize.reporter.app.viewModels.TaskViewModel;
import com.cyecize.reporter.common.controllers.BaseController;
import com.cyecize.reporter.users.RoleConstants;
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
import com.cyecize.summer.common.models.JsonResponse;
import com.cyecize.summer.common.models.ModelAndView;
import com.cyecize.summer.common.models.RedirectAttributes;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

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
                this.projectService.findByOwner(this.userService.findOneByUsername(principal.getUser().getUsername()))
        );
    }

    @PostMapping("/create")
    public ModelAndView createTaskPostAction(@Valid CreateTaskBindingModel bindingModel, BindingResult bindingResult,
                                             Principal principal, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("model", bindingModel);

        if (bindingResult.hasErrors()) {
            return super.redirect("create");
        }

        if (!principal.getUser().getUsername().equals(bindingModel.getProject().getOwner().getUsername())) {
            return super.redirect("/");
        }

        this.taskService.createTask(bindingModel);

        return super.redirect("my");
    }

    @GetMapping("/project/{projId}")
    public JsonResponse getTasksForProjectAction(@ConvertedBy(IdToProjectAdapter.class) @PathVariable("projId") Project project) {
        return new JsonResponse().addAttribute(
                "items",
                this.taskService.findAllByProject(project).stream()
                        .map(task -> this.modelMapper.map(task, TaskViewModel.class))
                        .collect(Collectors.toList())
        );
    }
}
