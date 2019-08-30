package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.bindingModels.EditProjectBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.app.repositories.ProjectRepository;
import com.cyecize.reporter.common.utils.ModelMerger;
import com.cyecize.reporter.users.entities.User;
import com.cyecize.summer.common.annotations.Autowired;
import com.cyecize.summer.common.annotations.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;

    private final ModelMapper modelMapper;

    private final ModelMerger modelMerger;

    @Autowired
    public ProjectServiceImpl(ProjectRepository repository, ModelMapper modelMapper, ModelMerger modelMerger) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.modelMerger = modelMerger;
    }

    @Override
    public void editProject(Project project, EditProjectBindingModel bindingModel) {
        this.modelMerger.merge(bindingModel, project);
        this.repository.merge(project);
    }

    @Override
    public void addParticipantToProject(Project project, User participant) {
        if (!this.isUserParticipant(project, participant)) {
            project.getParticipants().add(participant);
            this.repository.merge(project);
        }
    }

    @Override
    public boolean removeParticipantFromProject(Project project, User participant) {
        if (this.isUserParticipant(project, participant) && !this.isUserProjectOwner(participant, project)) {
            project.setParticipants(project.getParticipants().stream()
                    .filter(p -> !p.getId().equals(participant.getId()))
                    .collect(Collectors.toList())
            );

            this.repository.merge(project);
            return true;
        }

        return false;
    }

    private boolean isUserParticipant(Project project, User user) {
        return project.getParticipants().stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    private boolean isUserProjectOwner(User user, Project project) {
        return project.getOwner().getId().equals(user.getId());
    }

    @Override
    public Project findById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public Project createProject(CreateProjectBindingModel bindingModel, User owner) {
        Project project = this.modelMapper.map(bindingModel, Project.class);
        project.setOwner(owner);
        project.getParticipants().add(owner);

        this.repository.persist(project);
        return project;
    }

    @Override
    public List<Project> findByOwner(User owner, boolean skipCompleted) {
        return this.repository.findByOwner(owner, skipCompleted);
    }

    @Override
    public List<Project> findInvolved(User involvedUser, boolean skipCompleted) {
        return this.repository.findByParticipant(involvedUser, skipCompleted);
    }

    @Override
    public List<Project> findAll(boolean skipCompleted) {
        return this.repository.findAll(skipCompleted);
    }
}
