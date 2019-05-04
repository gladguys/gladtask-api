package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.repositories.TeamRepository;
import br.com.glad.gladtask.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

	private TeamRepository teamRepository;

	public TeamServiceImpl(TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Override
	public Team createOrUpdate(Team team) {
		return this.teamRepository.save(team);
	}

	@Override
	public List<Team> findAll() {
		return this.teamRepository.findAll();
	}

	@Override
	public Optional<Team> findById(String id) {
		return this.teamRepository.findById(id);
	}

	@Override
	public List<Team> findByParticipantsId(String userId) {
		return this.teamRepository.findByParticipantsId(userId);
	}

	@Override
	public List<Team> findByNameLikeAllIgnoreCase(String term) {
		return null;
	}
}
