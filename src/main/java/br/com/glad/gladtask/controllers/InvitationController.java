package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.entities.Invitation;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.dtos.InvitationDTO;
import br.com.glad.gladtask.services.InvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/api/invitations")
@Api(value = "Invitation Controller", description = "Perform operations regard gladtask invitation to teams")
public class InvitationController {

    InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @ApiOperation(value = "Create a invitation")
    @PostMapping
    public ResponseEntity<Invitation> create(HttpServletRequest request, @RequestBody InvitationDTO invitation) {
        try {

            return ResponseEntity.ok(this.invitationService.createOrUpdateByDTO(invitation));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
    }

    @ApiOperation(value = "Get invitations by user")
    @GetMapping(value = "/user-receiver/{userId}")
    public ResponseEntity<List<Invitation>> getInvitationsByUser(@PathVariable("userId") String userId) {
        try {
            return ResponseEntity.ok(this.invitationService.findAllByReceiverId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        try {
            this.invitationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
