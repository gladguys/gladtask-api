package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.entities.enums.StatusEnum;
import br.com.glad.gladtask.repositories.TaskRepository;
import br.com.glad.gladtask.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

	private final long dayInMillis = 86400000;
	private TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task createOrUpdate(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Task findById(String taskId) {
		return taskRepository.findById(taskId).get();
	}

	@Override
	public void delete(String taskId) {
		Task task = taskRepository.findById(taskId).get();
		taskRepository.delete(task);
	}

	@Override
	public Page<Task> findByCreatorUser(int page, int count, String userId) {
		Pageable pageable = new PageRequest(page, count);
		return taskRepository.findByCreatorUserIdOrderByCreationDateDesc(pageable, userId);
	}

	@Override
	public List<Task> findByTargetUser(String userId) {
		return taskRepository.findByTargetUserIdOrderByCreationDateDesc(userId);
	}

	@Override
	public Page<Task> findByTargetUserAndStatus(int page, int count, String userId, StatusEnum status) {
		Pageable pageable = new PageRequest(page, count);
		return taskRepository.findByTargetUserIdAndStatusOrderByCreationDateDesc(pageable, userId, status);
	}

	@Override
	public Page<Task> findByParameters(int page, int count, String title, String status, String prioridade) {
		Pageable pageable = new PageRequest(page, count);
		return taskRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByCreationDateDesc(pageable, title, status, prioridade);
	}

	@Override
	public Page<Task> findByParametersAndCreatorUser(int page, int count, String title, String status, String prioridade, String userId) {
		Pageable pageable = new PageRequest(page, count);
		return taskRepository.findByTitleIgnoreCaseContainingAndStatusAndPriorityAndCreatorUserIdOrderByCreationDateDesc(pageable, title, status, prioridade, userId);
	}

	@Override
	public Page<Task> findAll(int page, int count) {
		Pageable pageable = new PageRequest(page, count);
		return taskRepository.findAll(pageable);
	}

	@Override
	public List<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public List<Task> findTasksByTargetUserAndProject(String userId, String projectId) {
		return  taskRepository.findTasksByTargetUserIdAndProjectId(userId, projectId);
	}

	@Override
	public List<Task> findFirst4ByTargetUserIdOrderByLastEdited(String userId) {
		return  taskRepository.findFirst4ByTargetUserIdOrderByLastEditedDesc(userId);
	}

	@Override
	public List<Task> findByTitleOrDescriptionLikeAllIgnoreCase(String term) {
		return taskRepository.findByTitleLikeOrDescriptionLikeAllIgnoreCase(term, term);
    }

	private List<StatusEnum> getListOfRejectedStatus(StatusEnum... status) {
		List<StatusEnum> statusToAvoid = new ArrayList<>();
		for (StatusEnum se : status) {
			statusToAvoid.add(se);
		}
		return statusToAvoid;
	}

	@Override
	public List<Task> findBetweenDatesByTargetUser(Integer days, String userId) {
		Date today = new Date();
		Date dueDate = new Date(today.getTime() + days * dayInMillis);
		List<StatusEnum> listOfRejectedStatus = getListOfRejectedStatus(StatusEnum.CONCLUIDA);
		return this.taskRepository.findByDueDateLessThanAndTargetUserIdAndStatusNotIn(dueDate, userId,  listOfRejectedStatus).stream()
				.sorted((o1, o2) -> o1.getDueDate().after(o2.getDueDate()) ? +1 : -1)
				.collect(Collectors.toList());
	}

	@Override
	public List<Task> get50MostRecentTasksEditedByUser(String userId) {
		return this.taskRepository.findTo50ByTargetUserOrderByLastEdited(userId);
	}

	@Override
	public List<Task> findTasksByProject(String projectId) {
		return this.taskRepository.findTasksByProjectId(projectId);
	}

	@Override
	public List<Task> findTasksBasedOnText(String text) {
		return taskRepository.findByTitleQuery(text);
	}
}