package com.cyecize.reporter.app.services;

import com.cyecize.reporter.app.bindingModels.CreateProjectBindingModel;
import com.cyecize.reporter.app.entities.Project;
import com.cyecize.reporter.users.entities.User;

import java.util.List;

public interface ProjectService {

    Project findById(Long id);

    Project createProject(CreateProjectBindingModel bindingModel, User owner);

    List<Project> findByOwner(User owner);

    List<Project> findInvolved(User involvedUser);

    List<Project> findAll();

    //List<Project> findAll(boolean skipCompleted);
}
