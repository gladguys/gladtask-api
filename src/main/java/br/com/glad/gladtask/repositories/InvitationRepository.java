package br.com.glad.gladtask.repositories;

import br.com.glad.gladtask.entities.Invitation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvitationRepository extends MongoRepository<Invitation, String> {

    List<Invitation> findAllByReceiverIdAndIsActive(String receiverId, boolean isActive);
}
