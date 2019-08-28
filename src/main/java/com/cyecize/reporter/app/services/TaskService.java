package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;

import java.util.List;

public interface TaskService {

    Task createTask(CreateTaskBindingModel bindingModel);

    Task findById(Long id);

    List<Task> findMainTasks(Project project);

    List<Task> findAllByProject(Project project);
}
