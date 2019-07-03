package br.com.glad.gladtask.entities;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public @Data class TimeSpent {

	@ApiModelProperty(notes = "Gladname of the user that spent the hours")
	private String gladname;

	@ApiModelProperty(notes = "Date of the time spent register")
	private String date;

	@ApiModelProperty(notes = "The quantity of minutes spent on the task on this date")
	private Long minutesSpent;
}
