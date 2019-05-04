package br.com.glad.gladtask.services;

import br.com.glad.gladtask.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {

	User findByEmail(String email);
	User createOrUpdate(User user);
	User findById(String id);
	User findByUsername(String username);
	void delete(String id);
	Page<User> findAllPaged(int page, int count);
	List<User> findAll();
	List<User> findByFirstNameLikeOrLastNameLikeAllIgnoreCase(String term);
    List<User> findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(String term);
}