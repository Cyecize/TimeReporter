package com.cyecize.reporter.app.dataAdapters;

import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.services.TaskService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Component;

@Component
public class IdToTaskAdapter implements DataAdapter<Task> {

    private final TaskService taskService;

    @Autowired
    public IdToTaskAdapter(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Task resolve(String paramName, HttpSoletRequest httpSoletRequest) {
        String taskId = httpSoletRequest.getBodyParameters().get(paramName);

        try {
            return this.taskService.findById(Long.parseLong(taskId));
        } catch (Exception ignored) {
        }

        return null;
    }
}
