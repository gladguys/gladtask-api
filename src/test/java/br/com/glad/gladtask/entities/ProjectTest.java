package br.com.glad.gladtask.entities;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {

	Project project;

	@Before
	public void setUp() {
		project = new Project();
	}

	@Test
	public void getId() {
		String id = "1233fhdsk3r443d";
		project.setId(id);

		assertEquals(id, project.getId());
	}

	@Test
	public void getName() {
		String name = "Simple Project";
		project.setName(name);

		assertEquals(name, project.getName());
	}

	@Test
	public void getDescription() {
		String description = "Simple Project Description";
		project.setDescription(description);

		assertEquals(description, project.getDescription());
	}

	@Test
	public void getCreationDate() {
		Date creationDate = new Date();
		project.setCreationDate(creationDate);

		assertEquals(creationDate, project.getCreationDate());
	}

	@Test
	public void getProjectImage() {
		String projectImage = "ffiej893ru9832ujsddns";
		project.setProjectImage(projectImage);

		assertEquals(projectImage, project.getProjectImage());
	}
}
