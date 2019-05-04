package br.com.glad.gladtask.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.glad.gladtask.entities.Project;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.ProjectService;
import br.com.glad.gladtask.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/project")
@Api(value = "Project Controller", description = "Perform operations regard gladtask projects")
public class ProjectController {

	private ProjectService projectService;
	private TaskService taskService;
	private UserService userService;
	private JwtTokenUtil jwtTokenUtil;

	public ProjectController(ProjectService projectService, TaskService taskService, UserService userService, JwtTokenUtil jwtTokenUtil) {
		this.projectService = projectService;
		this.taskService = taskService;
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@ApiOperation(value = "Create a project")
	@PostMapping
	public ResponseEntity<Project> create(HttpServletRequest request,  @RequestBody Project project) {
		project.setCreationDate(new Date());
		return createOrUpdateProject(project);
	}

	@ApiOperation(value = "Update a project")
	@PutMapping
	public ResponseEntity<Project> update(@RequestBody Project project) {
		return createOrUpdateProject(project);
	}

	private ResponseEntity<Project> createOrUpdateProject(Project project) {
		try {
			return ResponseEntity.ok(projectService.createOrUpdate(project));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all projects")
	@GetMapping()
	public ResponseEntity<List<Project>> findAll() {
		try {
			return ResponseEntity.ok(projectService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all projects by team")
	@GetMapping(value = "/team/{teamId}")
	public ResponseEntity<List<Project>> findAllByTeam(@PathVariable("teamId") String teamId) {
		try {
			return ResponseEntity.ok(projectService.findAllByTeamId(teamId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find a project by it's id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Project> findById(@PathVariable("id") String id) {
		try {
			Optional<Project> projectFound = projectService.findById(id);
			if (projectFound.isPresent()) {
				return ResponseEntity.ok(projectFound.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find projects that the project name is like the term")
	@GetMapping(value = "name/{term}")
	public ResponseEntity<List<Project>> findByNameLikeAllIgnoreCase(@PathVariable("term") String term) {
		try {
			return ResponseEntity.ok(projectService.findByNameLikeAllIgnoreCase(term));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find projects of an user")
	@GetMapping(value = "/user/{userId}")
	public ResponseEntity<List<Project>> findByUser(@PathVariable("userId") String userId) {
		try {
			List<Project> recentProjects  = taskService.get50MostRecentTasksEditedByUser(userId)
														.stream()
														.map(Task::getProject)
														.filter(distinctProjectById(Project::getId))
														.collect(Collectors.toList());

			return ResponseEntity.ok(recentProjects);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private static <T> Predicate<T> distinctProjectById(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
}