package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;

import java.util.List;

public interface TaskService {

    List<Task> findMainTasks(Project project);
}
