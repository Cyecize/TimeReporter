package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.bindingModels.EditProjectBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.users.entities.User;

import java.util.List;

public interface ProjectService {

    void editProject(Project project, EditProjectBindingModel bindingModel);

    void addParticipantToProject(Project project, User participant);

    boolean removeParticipantFromProject(Project project, User participant);

    Project findById(Long id);

    Project createProject(CreateProjectBindingModel bindingModel, User owner);

    List<Project> findByOwner(User owner, boolean skipCompleted);

    List<Project> findInvolved(User involvedUser, boolean skipCompleted);

    List<Project> findAll(boolean skipCompleted);

    //List<Project> findAll(boolean skipCompleted);
}
