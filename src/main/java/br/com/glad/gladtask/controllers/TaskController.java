package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.entities.TaskChange;
import br.com.glad.gladtask.entities.TaskComment;
import br.com.glad.gladtask.entities.TimeSpent;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.enums.StatusEnum;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.TaskService;
import br.com.glad.gladtask.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/tasks")
@CrossOrigin(origins = "*")
@Api(value = "Task Controller", description = "Perform operations regard gladtask task")
public class TaskController {

	private TaskService taskService;
	private UserService userService;
	private JwtTokenUtil jwtTokenUtil;

	public TaskController(TaskService taskService, UserService userService, JwtTokenUtil jwtTokenUtil) {
		this.taskService = taskService;
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@ApiOperation(value = "Find a task by it's number")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Task> findById(@PathVariable("id") String id) {
		try {
			Task taskFound = taskService.findById(id);
			if (taskFound != null) {
				return ResponseEntity.ok(taskFound);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find a task that has the term in its title or description")
	@GetMapping(value = "/term/{term}")
	public ResponseEntity<List<Task>> findByTitleOrDescriptionLikeAllIgnoreCase(@PathVariable("term") String term) {
		try {
			List<Task> tasksFound = taskService.findByTitleOrDescriptionLikeAllIgnoreCase(term);
			if (tasksFound != null) {
				return ResponseEntity.ok(tasksFound);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Create a new task")
	@PostMapping
	public ResponseEntity<Task> create(HttpServletRequest request, @RequestBody Task task) {
		try {
			Date now = new Date();
			task.setCreatorUser(userFromRequest(request));
			task.setSenderUser(userFromRequest(request));
			task.setCreationDate(now);


			return ResponseEntity.ok(taskService.createOrUpdate(task));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Update a task")
	@PutMapping
	public ResponseEntity<Task> update(HttpServletRequest request, @RequestBody Task task) {
		Date now = new Date();
		task.getTaskChanges()
				.stream()
				.filter(taskChange -> taskChange.getDate() == null)
				.forEach(taskChange -> taskChange.setDate(now));

		try {
			task.setLastEdited(new Date());
			task.setSenderUser(userFromRequest(request));

			return ResponseEntity.ok(taskService.createOrUpdate(task));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Update a task status")
	@PutMapping(value = "/{taskId}/update-status")
	public ResponseEntity<Task> updateTaskStatus(HttpServletRequest request, @PathVariable("taskId") String taskId, @RequestBody String taskStatus) {
		try {
			Task task = taskService.findById(taskId);
			List<TaskChange> taskChanges = task.getTaskChanges();
			taskChanges.add(buildTaskChange(request, "Situação", StatusEnum.getStatus(taskStatus).getDescription(), task.getStatus().getDescription()));
			task.setStatus(StatusEnum.getStatus(taskStatus));
			task.setLastEdited(new Date());
			task.setSenderUser(userFromRequest(request));

			return ResponseEntity.ok(taskService.createOrUpdate(task));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private TaskChange buildTaskChange(HttpServletRequest request, String whatHasChanged, String newValue, String oldValue) {
		TaskChange taskChange = new TaskChange();
		taskChange.setDate(new Date());
		taskChange.setWhatHasChanged(whatHasChanged);
		taskChange.setNewValue(newValue);
		taskChange.setOldValue(oldValue);
		taskChange.setUserFirstName(userFromRequest(request).getFirstName());
		return taskChange;
	}

	@ApiOperation(value = "Save a comment of a task")
	@PostMapping(value = "/save-comment/{id}")
	public ResponseEntity<Task> saveComment(HttpServletRequest request, @PathVariable("id") String id, @RequestBody TaskComment taskComment) {
		Task task = taskService.findById(id);
		try {
			if (task != null) {
				List<TaskComment> taskComments = task.getTaskComments();
				taskComments.add(taskComment);
				task.setTaskComments(taskComments);
				return ResponseEntity.ok(taskService.createOrUpdate(task));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Save a time spent of a task")
	@PostMapping(value = "/{id}/save-time-spent")
	public ResponseEntity<Task> saveTimeSpent(HttpServletRequest request, @PathVariable("id") String id, @RequestBody TimeSpent timeSpent) {
		Task task = taskService.findById(id);
		try {
			if (task != null) {
				List<TimeSpent> taskTimeSpents = task.getTimeSpentValues();
				taskTimeSpents.add(timeSpent);
				task.setTimeSpentValues(taskTimeSpents);
				return ResponseEntity.ok(taskService.createOrUpdate(task));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all tasks paged")
	@GetMapping("/{page}/{qtd}")
	public ResponseEntity<List<Task>> findAllPaged(@PathVariable("page") int page, @PathVariable("qtd") int qtd) {
		try {
			Page<Task> tasksPaged = taskService.findAll(page, qtd);
			List<Task> tasks = tasksPaged.get().collect(Collectors.toList());
			return ResponseEntity.ok(tasks);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks between the dates")
	@GetMapping("/between/{days}")
	public ResponseEntity<List<Task>> findBetweenDates(@PathVariable("days") Integer days, @RequestParam String userId) {
		try {
			if (userId != null) {
				return ResponseEntity.ok(taskService.findBetweenDatesByTargetUser(days, userId));
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find all tasks")
	@GetMapping()
	public ResponseEntity<List<Task>> findAll() {
		try {
			return ResponseEntity.ok(taskService.findAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks of a given target user")
	@GetMapping(value = "/user-target/{id}")
	public ResponseEntity<List<Task>> findTasksByTargetUser(@PathVariable("id") String id) {
		try {
			return ResponseEntity.ok(taskService.findByTargetUser(id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks of a given project")
	@GetMapping(value = "/project/{projectId}")
	public ResponseEntity<List<Task>> findTasksByProject(@PathVariable("projectId") String projectId) {
		try {
			return ResponseEntity.ok(taskService.findTasksByProject(projectId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find the last 4 edited tasks for the user")
	@GetMapping(value = "/last-edited/{userId}")
	public ResponseEntity<List<Task>> findFirst4ByTargetUserIdOrderByLastEdited(@PathVariable("userId") String userId) {
		try {
			return ResponseEntity.ok(taskService.findFirst4ByTargetUserIdOrderByLastEdited(userId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks of a given target user and a given status")
	@GetMapping(value = "/user-target/{id}/{status}")
	public ResponseEntity<Page<Task>> findTasksByTargetUserAndStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
		try {
			return ResponseEntity.ok(
					taskService.findByTargetUserAndStatus(0, 10, id, StatusEnum.getStatus(status)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks of a given creator user")
	@GetMapping(value = "/user-creator/{id}")
	public ResponseEntity<Page<Task>> findTasksByCreatorUser(@PathVariable("id") String id) {
		try {
			return ResponseEntity.ok(taskService.findByCreatorUser(0, 10, id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@ApiOperation(value = "Find tasks of a given target user and a given project")
	@GetMapping(value = "/project/{userId}/{projectId}")
	public ResponseEntity<List<Task>> findTasksByTargetUserAndProject(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId) {
		try {
			return ResponseEntity.ok(taskService.findTasksByTargetUserAndProject(userId, projectId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	@ApiOperation(value = "Delete a task")
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		try {
			Task taskFound = taskService.findById(id);
			if (taskFound != null) {
				taskService.delete(id);
			} else {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(value = "/similar")
	public ResponseEntity<List<Task>> getSimilarsTaksByTitle(@RequestParam("title") String title) {
		List<String> tags = Arrays.asList(title.split(" "));
		String text = tags.stream().filter(tag -> tag.length() > 3)
				.map( tag -> "\\'"+tag+"\\' ").reduce("", String::concat);

		List<Task> tasks = taskService.findTasksBasedOnText(text);
		tags.forEach(t -> tasks.removeIf(task -> !task.getTitle().contains(t)));

		return ResponseEntity.ok(tasks);
	}
}
