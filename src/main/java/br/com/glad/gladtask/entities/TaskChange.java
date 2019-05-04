package br.com.glad.gladtask.entities;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public @Data class TaskChange {
	@ApiModelProperty(notes = "The name of the user that performed the task change")
	private String userFirstName;

	@ApiModelProperty(notes = "Date of the change")
	private Date date;

	@ApiModelProperty(notes = "What has changed in the task")
	private String whatHasChanged;

	@ApiModelProperty(notes = "The old value of the field")
	private String oldValue;

	@ApiModelProperty(notes = "The new value of the field")
	private String newValue;
}