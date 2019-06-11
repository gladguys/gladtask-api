package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.repositories.TeamRepository;
import br.com.glad.gladtask.repositories.UserRepository;
import br.com.glad.gladtask.services.TeamService;
import br.com.glad.gladtask.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private TeamRepository teamRepository;


	public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository) {
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
	}

	@Override
	public User findByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public List<User> findByTeam(String teamId) throws Exception {
		Team team = this.teamRepository.findById(teamId).orElseThrow(Exception::new);
		return team.getParticipants();
	}

	@Override
	public User createOrUpdate(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(String id) {
		return this.userRepository.findById(id).get();
	}

	@Override
	public User findByUsername(String username) {
		return this.userRepository.findByUsername(username);
	}

	@Override
	public void delete(String id) {
		User userToBeDeleted = this.userRepository.findById(id).get();
		this.userRepository.delete(userToBeDeleted);
	}

	@Override
	public Page<User> findAllPaged(int page, int count) {
		Pageable pageable = new PageRequest(page, count);
		return this.userRepository.findAll(pageable);
	}

	@Override
	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	@Override
	public List<User> findByFirstNameLikeOrLastNameLikeAllIgnoreCase(String term) {
		return this.userRepository.findByFirstNameLikeOrLastNameLikeAllIgnoreCase(term, term);
	}

	@Override
	public List<User> findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(String term) {
		return this.userRepository.findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(term, term, term);
	}
}
