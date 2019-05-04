package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.entities.Invitation;
import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.dtos.InvitationDTO;
import br.com.glad.gladtask.repositories.InvitationRepository;
import br.com.glad.gladtask.repositories.TeamRepository;
import br.com.glad.gladtask.repositories.UserRepository;
import br.com.glad.gladtask.services.InvitationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationServiceImpl implements InvitationService {

    InvitationRepository invitationRepository;
    UserRepository userRepository;
    TeamRepository teamRepository;

    public InvitationServiceImpl(InvitationRepository invitationRepository, UserRepository userRepository, TeamRepository teamRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Invitation createOrUpdateByDTO(InvitationDTO invitationDTO) throws Exception {
        Invitation invitationToSave = new Invitation();
        User author = this.userRepository.findById(invitationDTO.getAuthorUserId()).orElseThrow(Exception::new);
        invitationToSave.setAuthor(author);

        User receiver = this.userRepository.findById(invitationDTO.getReceiverUserId()).orElseThrow(Exception::new);
        invitationToSave.setReceiver(receiver);

        Team team = this.teamRepository.findById(invitationDTO.getTeamId()).orElseThrow(Exception::new);
        invitationToSave.setTeam(team);
        invitationToSave.setActive(true);
        return this.invitationRepository.save(invitationToSave);

    }

    @Override
    public Invitation createOrUpdate(Invitation invitation) {
        return this.invitationRepository.save(invitation);
    }

    @Override
    public List<Invitation> findAllByReceiverId(String userId) {
        return this.invitationRepository.findAllByReceiverIdAndIsActive(userId, true);
    }

    @Override
    public Optional<Invitation> findById(String id) {
        return this.invitationRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        this.invitationRepository.deleteById(id);
    }
}
