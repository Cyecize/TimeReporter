package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Report;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.repositories.TaskRepository;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    private final ModelMapper modelMapper;

    private final ReportService reportService;

    @Autowired
    public TaskServiceImpl(TaskRepository repository, ModelMapper modelMapper, ReportService reportService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.reportService = reportService;
    }

    @Override
    public Pair<Long, Long> findTotalReportedTimeForTask(Task task) {
        return this.reportService.findTotalReportedTimeForTask(task);
    }

    @Override
    public Pair<Long, Long> findTotalReportedTimeForTaskRecursive(Task task) {
        Long minutes = this.findTotalReportedMinutesForTaskRecursive(task, 0L);
        return new Pair<>(minutes / 60, minutes % 60);
    }

    private Long findTotalReportedMinutesForTaskRecursive(Task task, Long minutes) {
        Pair<Long, Long> totalReportedTimeForTask = this.findTotalReportedTimeForTask(task);
        minutes += totalReportedTimeForTask.getValue();
        minutes += totalReportedTimeForTask.getKey() * 60;

        for (Task subTask : task.getSubTasks()) {
            this.findTotalReportedMinutesForTaskRecursive(subTask, minutes);
        }

        return minutes;
    }

    @Override
    public Task createTask(CreateTaskBindingModel bindingModel) {
        Task task = this.modelMapper.map(bindingModel, Task.class);

        this.repository.persist(task);

        return task;
    }

    @Override
    public Task findById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public List<Task> findMainTasks(Project project) {
        return this.repository.findByNullParentAndProject(project);
    }

    @Override
    public List<Task> findAllByProject(Project project) {
        return this.repository.findByProject(project);
    }

    @Override
    public List<Task> findAllTasksForUser(User user) {
        final Map<Long, Task> tasks = new HashMap<>();

        for (Report report : this.reportService.findByReporter(user)) {
            final Task task = report.getTask();
            if (!tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
            }
        }

        return new ArrayList<>(tasks.values());
    }
}
