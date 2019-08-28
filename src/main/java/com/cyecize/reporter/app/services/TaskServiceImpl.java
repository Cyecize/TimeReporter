package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateTaskBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.entities.Task;
import com.cyecize.reporter.app.repositories.TaskRepository;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;
import org.modelmapper.ModelMapper;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    private final ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
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
}
