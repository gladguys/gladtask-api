package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.TestUtil;
import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TaskServiceImplTest {

	TaskServiceImpl taskService;
	@Mock
	TaskRepository taskRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		taskService = new TaskServiceImpl(taskRepository);
	}

	@Test
	public void createOrUpdate() {
		Task task = new Task();
		String id = "2333dedefrgrtswe45fs";
		task.setId(id);

		when(taskRepository.save(task)).thenReturn(task);

		Task taskFromMock = taskService.createOrUpdate(task);
		assertEquals(task.getId(), taskFromMock.getId());
	}

	@Test
	public void findById() {
		Task task = new Task();
		String id = "2333dedefrgrtswe45fs";

		when(taskRepository.findById(id)).thenReturn(Optional.of(task));
		Task taskById = taskService.findById(id);
		assertEquals(taskById.getId(), task.getId());
	}

	@Test
	public void listTasks() {
		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);

		Pageable pageable = new PageRequest(0, 10);
		when(taskRepository.findAll(pageable)).thenReturn(tasksPaged);

		assertEquals(taskService.findAll(0,10).get().count(), 2);
	}

	@Test
	public void createStatusChange() {

	/*	StatusChange statusChange = new StatusChange();
		String id = "dede44333dedefrf33re";
		statusChange.setId(id);

		when(statusChangeRepository.save(statusChange)).thenReturn(statusChange);

		assertEquals(taskService.createStatusChange(statusChange), statusChange);*/
	}

	@Test
	public void listStatusChanges() {
		/*String taskId = "deddededfwegetgeget";

		StatusChange sc1 = new StatusChange();
		String id = "3d3d3d3d3d33d3";
		sc1.setId(id);

		StatusChange sc2 = new StatusChange();
		String id2 = "d1d1d1d12d2d2d";
		sc2.setId(id2);

		List<StatusChange> statusChangeStream = new ArrayList<>();
		statusChangeStream.add(sc1);
		statusChangeStream.add(sc2);

		Iterable<StatusChange> statusChangeIterable = statusChangeStream;

		when(statusChangeRepository.findByTaskIdOrderByDateDesc(taskId)).thenReturn(statusChangeIterable);

		assertEquals(taskService.listStatusChanges(taskId), statusChangeIterable);*/
	}

	@Test
	public void findByCreatorUser() {
		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);

		String userId = "3e3defergafwrg3fwrg4333fsg";
		Pageable pageable = new PageRequest(0, 20);

		when(taskRepository.findByCreatorUserIdOrderByCreationDateDesc(pageable,userId)).thenReturn(tasksPaged);

		assertEquals(tasksPaged.getTotalPages(), taskService.findByCreatorUser(0,20,userId).getTotalPages());
		assertEquals(tasksPaged.getTotalElements(), taskService.findByCreatorUser(0,20,userId).getTotalElements());
	}

	@Test
	public void findByTargetUser() {
		List<Task> tasks = TestUtil.montaListaTasks();

		String userId = "3e3defergafwrg3fwrg4333fsg";

		when(taskRepository.findByTargetUserIdOrderByCreationDateDesc(userId)).thenReturn(tasks);

		List<Task> tasksByTargetUser = taskService.findByTargetUser(userId);

		assertEquals(tasks.size(), tasksByTargetUser.size());
	}

	@Test
	public void findByParameters() {
		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);
		Pageable pageable = new PageRequest(0, 10);
		String title = "title";
		String status = "status";
		String priority = "priority";

		when(taskRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByCreationDateDesc(pageable,title, status,priority))
				.thenReturn(tasksPaged);

		assertEquals(tasksPaged.getTotalElements(), taskService.findByParameters(0,10,title,status, priority).getTotalElements());
	}

	@Test
	public void findByParametersAndCreatorUser() {

		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);
		Pageable pageable = new PageRequest(0, 10);
		String title = "title";
		String status = "status";
		String priority = "priority";
		String creatorUserId= "dewfwrge2r24224fefv244";

		when(taskRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndCreatorUserIdOrderByCreationDateDesc(pageable,title, status,priority,creatorUserId))
				.thenReturn(tasksPaged);

		assertEquals(tasksPaged.getTotalElements(), taskService.findByParametersAndCreatorUser(0,10,title,status, priority,creatorUserId).getTotalElements());
	}

	@Test
	public void findByParametersAndTargetUser() {
		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);
		Pageable pageable = new PageRequest(0, 10);
		String title = "title";
		String status = "status";
		String priority = "priority";
		String targetUserId= "dewfwrge2r24224fefv244";

		when(taskRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndCreatorUserIdOrderByCreationDateDesc(pageable,title, status,priority,targetUserId))
				.thenReturn(tasksPaged);


		assertEquals(tasksPaged.getTotalElements(), taskService.findByParametersAndCreatorUser(0,10,title,status, priority,targetUserId).getTotalElements());
	}

	@Test
	public void findAll() {
		List<Task> tasks = TestUtil.montaListaTasks();

		when(taskRepository.findAll()).thenReturn(tasks);

		assertEquals(tasks.size(), taskService.findAll().size());
		assertEquals(tasks.get(0), taskService.findAll().get(0));
	}

	@Test
	public void findAllPaged() {
		List<Task> tasks = TestUtil.montaListaTasks();

		Page<Task> tasksPaged = new PageImpl<>(tasks);
		Pageable pageable = new PageRequest(0, 10);

		when(taskRepository.findAll(pageable)).thenReturn(tasksPaged);

		assertEquals(tasksPaged.getTotalElements(), taskService.findAll(0,10).getTotalElements());
	}
}