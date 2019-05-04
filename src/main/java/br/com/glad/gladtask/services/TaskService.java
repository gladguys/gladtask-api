package br.com.glad.gladtask.services;

import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.entities.enums.StatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskService {

	Task createOrUpdate(Task task);
	Task findById(String taskId);
	void delete(String taskId);
	Page<Task> findByCreatorUser(int page, int count, String userId);
	List<Task> findByTargetUser(String userId);
	Page<Task> findByTargetUserAndStatus(int page, int count, String userId, StatusEnum status);
	Page<Task> findByParameters(int page, int count, String titulo, String status, String prioridade);
	Page<Task> findByParametersAndCreatorUser(int page, int count, String titulo, String status, String prioridade, String userId);
	Page<Task> findAll(int page, int count);
	List<Task> findAll();
	List<Task> findTasksByTargetUserAndProject(String userId, String projectId);
	List<Task> findFirst4ByTargetUserIdOrderByLastEdited(String userId);
	List<Task> findByTitleOrDescriptionLikeAllIgnoreCase(String term);
	List<Task> findTasksBasedOnText(String text);
    List<Task> findBetweenDatesByTargetUser(Integer days,  String userId);
    List<Task> get50MostRecentTasksEditedByUser(String userId);
	List<Task> findTasksByProject(String projectId);
}