package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.entities.Team;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.services.TeamService;
import br.com.glad.gladtask.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/api/users")
@CrossOrigin( origins = "*")
@Api(value = "User Controller", description = "Perform operations regard gladtask user")
public class UserController {

	private UserService userService;
	private TeamService teamService;
	private PasswordEncoder passwordEncoder;

	public UserController(UserService userService, TeamService teamService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.teamService = teamService;
		this.passwordEncoder = passwordEncoder;
	}

	@ApiOperation(value = "Create a new user")
	@PostMapping
	@ResponseBody
	public ResponseEntity<User> create(HttpServletRequest request, @RequestBody ObjectNode json) {
		try {
			ObjectMapper jsonObjectMapper = new ObjectMapper();
			User userFromRequest = jsonObjectMapper.treeToValue(json.get("user"), User.class);

			final User savedUser = createOrUpdateUser(userFromRequest);

			String teamId = json.get("teamId") != null ? json.get("teamId").asText() : null;
			if (teamId != null) {
				teamService.findById(teamId).ifPresent(team -> {
					team.getParticipants().add(savedUser);
					teamService.createOrUpdate(team);
				});
			} else {
				Team team = new Team();
				team.setManager(savedUser);
				team.setName("Meu time");
				team.setParticipants(Collections.singletonList(savedUser));
				teamService.createOrUpdate(team);
			}
			return ResponseEntity.ok(savedUser);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Update a user")
	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> update(@RequestBody User user) {
		try {
			return ResponseEntity.ok(createOrUpdateUser(user));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private User createOrUpdateUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userService.createOrUpdate(user);
	}

	@ApiOperation(value = "Find a user by it's id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") String id) {
		try {
			User userFound = userService.findById(id);
			return userFound != null ? ResponseEntity.ok(userFound) : ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find a user by it's username")
	@GetMapping(value = "/username/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable("username") String username) {
		try {
			User userFound = userService.findByUsername(username);
			return userFound != null ? ResponseEntity.ok(userFound) : ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find a user by it's email")
	@GetMapping(value = "/email/{email}/")
	public ResponseEntity<User> findByEmail(@PathVariable("email") String email) {
		try {
			User userFound = userService.findByEmail(email);
			return userFound != null ? ResponseEntity.ok(userFound) : ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find users that has first name or last name like the term")
	@GetMapping(value = "/any/{term}")
	public ResponseEntity<List<User>> findByFirstNameLikeOrLastNameLikeAllIgnoreCase(@PathVariable("term") String term) {
		try {
			List<User> usersFound = userService.findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(term);
			return usersFound != null ? ResponseEntity.ok(usersFound) : ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find users that has first name or last name like the term")
	@GetMapping(value = "/term/{term}")
	public ResponseEntity<List<User>> findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(@PathVariable("term") String term) {
		try {
			List<User> usersFound = userService.findByFirstNameLikeOrLastNameOrEmailLikeAllIgnoreCase(term);
			return usersFound != null ? ResponseEntity.ok(usersFound) : ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Delete a user")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		try {
			User userFound = userService.findById(id);
			if (userFound != null) {
				userService.delete(id);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all users paged")
	@GetMapping(value = "/{page}/{qtd}")
	public ResponseEntity<List<User>> allUsersPaged(@PathVariable("page") int page, @PathVariable("qtd") int qtd) {
		try {
			Page<User> usersPaged = userService.findAllPaged(page, qtd);
			List<User> users = usersPaged.get().collect(Collectors.toList());
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all tasks")
	@GetMapping()
	public ResponseEntity<List<User>> allUsers() {
		try {
			return ResponseEntity.ok(userService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}