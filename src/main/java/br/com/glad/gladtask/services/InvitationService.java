package br.com.glad.gladtask.services;

import br.com.glad.gladtask.entities.Invitation;
import br.com.glad.gladtask.entities.dtos.InvitationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface InvitationService {

    Invitation createOrUpdateByDTO(InvitationDTO invitation) throws Exception;
    Invitation createOrUpdate(Invitation invitation) throws Exception;
    List<Invitation> findAllByReceiverId(String userId);
    Optional<Invitation> findById(String id);
    void deleteById(String id);
}
