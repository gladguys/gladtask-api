package br.com.glad.gladtask.repositories;

import br.com.glad.gladtask.entities.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String> {

    List<Team> findByParticipantsId(String userId);
}
