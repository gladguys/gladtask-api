package br.com.glad.gladtask.repositories;

import java.util.Date;
import java.util.List;

import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.entities.enums.StatusEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TaskRepository extends MongoRepository<Task, String> {

	Page<Task> findByCreatorUserIdOrderByCreationDateDesc(Pageable pageable, String creatorUserId);
	List<Task> findByTargetUserIdOrderByCreationDateDesc(String targetUserId);
	Page<Task> findByTargetUserIdAndStatusOrderByCreationDateDesc(Pageable pageable, String targetUserId, StatusEnum status);
	Page<Task> findByTitleIgnoreCaseContainingAndStatusAndPriorityAndCreatorUserIdOrderByCreationDateDesc(Pageable pageable, String title, String status, String priority, String creatorUserId);
	Page<Task> findByTitleIgnoreCaseContainingAndStatusAndPriorityOrderByCreationDateDesc(Pageable pageable, String title, String status, String priority);
	List<Task> findTasksByTargetUserIdAndProjectId(String userId, String projectId);
	List<Task> findByTitleLikeOrDescriptionLikeAllIgnoreCase(String term, String term2);

	List<Task> findFirst4ByTargetUserIdOrderByLastEditedDesc(String userId);

	@Query("{ $text : { $search: ?0 } }")
	List<Task> findByTitleQuery(String text);

	//@Query("{ '$and' : [{ 'dueDate' : { $lt: ?0 } }, {'status' : {$ne : ['Concluída', 'Rejeitada']}}]}")
	List<Task> findByDueDateLessThanAndTargetUserIdAndStatusNotIn(Date dueDate,String userId, List<StatusEnum> statusToAvoid);

	List<Task> findTo50ByTargetUserOrderByLastEdited(String userId);
	List<Task> findTasksByProjectId(String projectId);
}