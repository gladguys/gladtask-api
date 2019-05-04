package br.com.glad.gladtask.entities;

import br.com.glad.gladtask.entities.enums.PrioridadeEnum;
import br.com.glad.gladtask.entities.enums.StatusEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TaskTest {

	private Task task;

	@Before
	public void setUp() {
		task = new Task();
	}

	@Test
	public void getId() {
		String id = "23444hfdjd5h3jdrg444fddws";
		task.setId(id);

		assertEquals(id, task.getId());
	}

	@Test
	public void getCreatorUser() {
		User user = createAUserWithOnlyId();
		task.setCreatorUser(user);

		assertEquals(user, task.getCreatorUser());
	}

	@Test
	public void getTargetUser() {
		User user = createAUserWithOnlyId();
		task.setTargetUser(user);

		assertEquals(user, task.getTargetUser());
	}

	@Test
	public void getCreationDate() {
		Date today = new Date();
		task.setCreationDate(today);

		assertEquals(today, task.getCreationDate());
	}

	@Test
	public void getTitle() {
		String title = "A title";
		task.setTitle(title);

		assertEquals(title, task.getTitle());
	}

	@Test
	public void getStatus() {
		task.setStatus(StatusEnum.CRIADA);

		assertEquals(StatusEnum.CRIADA, task.getStatus());
	}

	@Test
	public void getPriority() {
		PrioridadeEnum prioridadeEnum = PrioridadeEnum.Alto;
		task.setPriority(prioridadeEnum);

		assertEquals(PrioridadeEnum.Alto, task.getPriority());
	}

	@Test
	public void getDescription() {
		String description = "this is a glad description made only for testing this.";
		task.setDescription(description);

		assertEquals(description,task.getDescription());
	}

	@Test
	public void getImage() {
	}

	@Test
	public void getStatusChanges() {
		TaskChange tc1 = new TaskChange();
		TaskChange tc2 = new TaskChange();
		List<TaskChange> tcs = new ArrayList<>();
		tcs.add(tc1);
		tcs.add(tc2);

		task.setTaskChanges(tcs);

		assertEquals(task.getTaskChanges().get(0), tc1);
		assertEquals(task.getTaskChanges().get(1), tc2);
	}

	private User createAUserWithOnlyId() {
		User user = new User();
		String idUser = "dedefrfrgt55445gtgt";
		user.setId(idUser);
		return user;
	}

}