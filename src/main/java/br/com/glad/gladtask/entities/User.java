package br.com.glad.gladtask.entities;

import br.com.glad.gladtask.entities.enums.ProfileEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class User {
	@Id
	@ApiModelProperty(notes = "The database generated project ID")
	private String id;

	@ApiModelProperty(notes = "First name of user")
	private String firstName;

	@ApiModelProperty(notes = "Last name of user")
	private String lastName;

	@ApiModelProperty(notes = "Username")
	@NotBlank(message = "username deve ser informado.")
	private String username;

	@ApiModelProperty(notes = "Email of the user")
	@NotBlank(message = "Email deve ser informado.")
	@Email
	private String email;

	@ApiModelProperty(notes = "Password of the user")
	@NotBlank(message = "Password deve ser informado")
	@Size(min = 6)
	private String password;

	@ApiModelProperty(notes = "Profile of the user")
	private ProfileEnum profileEnum;

	@ApiModelProperty(notes = "User photo")
	private String profilePhoto;

	@ApiModelProperty(notes = "The teams that the users is part of")
	private List<Team> teams;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) &&
				Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}
}