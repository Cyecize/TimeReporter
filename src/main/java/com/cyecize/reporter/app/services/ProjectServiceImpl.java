package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.repositories.ProjectRepository;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;

    private final ModelMapper modelMapper;

    private final ReportService reportService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository, ModelMapper modelMapper, ReportService reportService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.reportService = reportService;
    }

    @Override
    public Project findById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public Project createProject(CreateProjectBindingModel bindingModel, User owner) {
        Project project = this.modelMapper.map(bindingModel, Project.class);
        project.setOwner(owner);

        this.repository.persist(project);
        return project;
    }

    @Override
    public List<Project> findByOwner(User owner) {
        return this.repository.findByOwner(owner);
    }

    @Override
    public List<Project> findInvolved(User involvedUser) {
        final Map<Long, Project> projects = this.findByOwner(involvedUser)
                .stream()
                .collect(Collectors.toMap(Project::getId, project -> project));

        this.reportService.findByReporter(involvedUser).forEach(report -> {
            final Project project = report.getTask().getProject();
            if (!projects.containsKey(project.getId())) {
                projects.put(project.getId(), project);
            }
        });

        return new ArrayList<>(projects.values());
    }

    @Override
    public List<Project> findAll() {
        return this.repository.findAll();
    }
}
