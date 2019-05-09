package br.com.glad.gladtask.entities;

import br.com.glad.gladtask.entities.enums.PrioridadeEnum;
import br.com.glad.gladtask.entities.enums.StatusEnum;
import br.com.glad.gladtask.entities.enums.TaskType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document
public @Data class Task {

	@Id
	@ApiModelProperty(notes = "The database generated project ID")
	private String id;

	@DBRef(lazy = true)
	@ApiModelProperty(notes = "User that created the task")
	private User creatorUser;

	@DBRef(lazy = true)
	@ApiModelProperty(notes = "User that sent the task")
	private User senderUser;

	@DBRef(lazy = true)
	@Indexed
	@ApiModelProperty(notes = "User assigned to do the task")
	private User targetUser;

	@DBRef()
	@ApiModelProperty(notes = "Project of the task")
	private Project project;

	@ApiModelProperty(notes = "Creation date of the task")
	private Date creationDate;

	@Indexed
	@ApiModelProperty(notes = "Title of the task")
	private String title;

	@ApiModelProperty(notes = "Current status of the task")
	private StatusEnum status;

	@ApiModelProperty(notes = "Priority of the task")
	private PrioridadeEnum priority;

	@ApiModelProperty(notes = "Description of the task")
	private String description;

	@ApiModelProperty(notes = "Image of the task")
	private String image;

	@ApiModelProperty(notes = "Type of the task")
	private TaskType taskType;

	@ApiModelProperty(notes = "The due date of the task")
	private Date dueDate;

	@ApiModelProperty(notes = "The date of the last time this task was edited")
	private Date lastEdited;

	@ApiModelProperty(notes = "Estimation of duration of the task in hours and minutes")
	private String estimatedTime;

	@ApiModelProperty(notes = "List of all the changes of the task")
	private List<TaskChange> taskChanges = new ArrayList<>();

	@ApiModelProperty(notes = "List of all the comments of the task")
	private List<TaskComment> taskComments = new ArrayList<>();

	@ApiModelProperty(notes = "List of all the time spent values of the task")
	private List<TimeSpent> timeSpentValues = new ArrayList<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return Objects.equals(id, task.id) &&
				Objects.equals(title, task.title) &&
				status == task.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, status);
	}
}