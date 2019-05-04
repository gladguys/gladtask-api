package br.com.glad.gladtask.repositories;

import java.util.List;

import br.com.glad.gladtask.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	User findByEmail(String email);
	User findByUsername(String username);
	List<User> findByFirstNameLikeOrLastNameLikeAllIgnoreCase(String term, String term2);
	List<User> findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(String term, String term2, String term3);
}