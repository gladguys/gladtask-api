package br.com.glad.gladtask.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.glad.gladtask.entities.Project;

@Component
public interface ProjectService {
	Project createOrUpdate(Project project);
	List<Project> findAll();
	List<Project> findAllByTeamId(String teamId);
	Optional<Project> findById(String id);
	List<Project> findByNameLikeAllIgnoreCase(String term);
}
