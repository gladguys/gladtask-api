package br.com.glad.gladtask.entities;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;

public @Data class Project {
	@Id
	@ApiModelProperty(notes = "The database generated project ID")
	private String id;

	@ApiModelProperty(notes = "Name of the project")
	private String name;

	@ApiModelProperty(notes = "Description of the project")
	private String description;

	@ApiModelProperty(notes = "Creation date of the project")
	private Date creationDate;

	@ApiModelProperty(notes = "A image for the project")
	private String projectImage;

	@DBRef
	@ApiModelProperty(notes = "A team for the project")
	private Team team;

	@DBRef
	private List<User> participants;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Project project = (Project) o;
		return Objects.equals(id, project.id) &&
				Objects.equals(name, project.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}