package br.com.glad.gladtask.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.glad.gladtask.entities.Project;
import br.com.glad.gladtask.repositories.ProjectRepository;
import br.com.glad.gladtask.services.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private ProjectRepository projectRepository;

	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Override
	public Project createOrUpdate(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public List<Project> findAll() {
		return projectRepository.findAll();
	}

	@Override
	public List<Project> findAllByTeamId(String teamId) {
		return this.projectRepository.findByTeamId(teamId);
	}

	@Override
	public Optional<Project> findById(String id) {
		return projectRepository.findById(id);
	}

	@Override
	public List<Project> findByNameLikeAllIgnoreCase(String term) {
		return projectRepository.findByNameLikeAllIgnoreCase(term);
	}
}
