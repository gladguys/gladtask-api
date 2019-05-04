package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.entities.Invitation;
import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.dtos.InvitationDTO;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.InvitationService;
import br.com.glad.gladtask.services.TeamService;
import br.com.glad.gladtask.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/teams")
@Api(value = "Team Controller", description = "Perform operations regard gladtask teams")
public class TeamController {

	private TeamService teamService;
	private UserService userService;
	private InvitationService invitationService;
	private JwtTokenUtil jwtTokenUtil;

	public TeamController(TeamService teamService, UserService userService, InvitationService invitationService, JwtTokenUtil jwtTokenUtil) {
		this.teamService = teamService;
		this.userService = userService;
		this.invitationService = invitationService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@ApiOperation(value = "Create a project")
	@PostMapping
	public ResponseEntity<Team> create(HttpServletRequest request, @RequestBody Team team) {
		User userCreator = userFromRequest(request);
		team.setManager(userCreator);
		team.addParticipant(userCreator);
		return createOrUpdateProject(team);

	}

	@ApiOperation(value = "Update a team")
	@PutMapping
	public ResponseEntity<Team> update(@RequestBody Team team) {
		return createOrUpdateProject(team);
	}

	private ResponseEntity<Team> createOrUpdateProject(Team team) {
		try {
			return ResponseEntity.ok(teamService.createOrUpdate(team));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	@GetMapping(value = "/user/{userId}")
	public ResponseEntity<List<Team>> getByUser(@PathVariable("userId") String userId) {
		try {
			return ResponseEntity.ok(this.teamService.findByParticipantsId(userId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<Team> getById(@PathVariable("id") String id) {
		try {
			Optional<Team> team = this.teamService.findById(id);
			if (team.isPresent()) {
				return ResponseEntity.ok(team.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value = "/add-user")
	public ResponseEntity addUserToTeam(@RequestBody InvitationDTO invitationDTO) {
		try {
			User userToAdd = this.userService.findById(invitationDTO.getReceiverUserId()) ;
			Team team = this.teamService.findById(invitationDTO.getTeamId()).orElseThrow(Exception::new);

			if (!team.getParticipants().contains(userToAdd)) {
				addUserToTeam(userToAdd, team);
				setInvitationToNotActive(invitationDTO);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void setInvitationToNotActive(@RequestBody InvitationDTO invitationDTO) throws Exception {
		Invitation invitationToConfirm = this.invitationService.findById(invitationDTO.getId()).orElseThrow(Exception::new);
		invitationToConfirm.setActive(false);
		this.invitationService.createOrUpdate(invitationToConfirm);
	}

	private void addUserToTeam(User userToAdd, Team team) {
		team.getParticipants().add(userToAdd);
		this.teamService.createOrUpdate(team);
	}
}