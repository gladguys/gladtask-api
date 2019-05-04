package br.com.glad.gladtask.entities;

import br.com.glad.gladtask.entities.enums.StatusEnum;
import lombok.Data;

public @Data class TaskStatus {
	private String taskId;
	private StatusEnum status;
}
