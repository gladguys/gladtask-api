package br.com.glad.gladtask.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Team {

	@Id
	private String id;

	private String name;

	@DBRef
	private List<User> participants;

	@DBRef
	private User manager;

    public void addParticipant(User user) {
    	this.participants.add(user);
	}
}
