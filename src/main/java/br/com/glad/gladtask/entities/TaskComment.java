package br.com.glad.gladtask.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public @Data class TaskComment {
	@DBRef(lazy = true)
	@ApiModelProperty(notes = "The user that made the comment")
	private User user;

	@ApiModelProperty(notes = "Date of the comment")
	private Date date;

	@ApiModelProperty(notes = "The text of the comment")
	private String text;
}