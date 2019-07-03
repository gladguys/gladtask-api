package br.com.glad.gladtask.repositories;

import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String> {

    List<Team> findByParticipantsId(String userId);
}
