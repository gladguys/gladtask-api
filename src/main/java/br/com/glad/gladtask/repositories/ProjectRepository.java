package br.com.glad.gladtask.repositories;

import java.util.List;

import br.com.glad.gladtask.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.glad.gladtask.entities.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
	List<Project> findByNameLikeAllIgnoreCase(String term);
	List<Project> findByTeamId(String teamId);
	List<Project> findTop3ByParticipants(User user);
}