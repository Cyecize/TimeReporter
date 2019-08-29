package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.bindingModels.EditTaskBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.common.utils.Pair;
import com.cyecize.reporter.users.entities.User;

import java.util.List;

public interface TaskService {

    void editTask(Task task, EditTaskBindingModel bindingModel);

    Pair<Long, Long> findTotalReportedTimeForTask(Task task);

    Pair<Long, Long> findTotalReportedTimeForTask(Task task, User user);

    Pair<Long, Long> findTotalReportedTimeForTaskRecursive(Task task);

    Task createTask(CreateTaskBindingModel bindingModel);

    Task findById(Long id);

    List<Task> findMainTasks(Project project);

    List<Task> findAllByProject(Project project);

    List<Task> findAllTasksForUser(User user);
}
