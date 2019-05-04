package br.com.glad.gladtask.services;

import br.com.glad.gladtask.entities.Team;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TeamService {
	Team createOrUpdate(Team team);
	List<Team> findAll();
	Optional<Team> findById(String id);
	List<Team> findByParticipantsId(String userId);
	List<Team> findByNameLikeAllIgnoreCase(String term);
}
