package br.com.glad.gladtask.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.glad.gladtask.TestUtil;
import br.com.glad.gladtask.entities.Project;
import br.com.glad.gladtask.repositories.ProjectRepository;

public class ProjectServiceImplTest {

	ProjectServiceImpl projectService;
	@Mock ProjectRepository projectRepository;

	Project project;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectService = new ProjectServiceImpl(projectRepository);
		project = new Project();
	}

	@Test
	public void createOrUpdate() {
		String id = "2333dedefrgrtswe45fs";
		project.setId(id);

		when(projectRepository.save(project)).thenReturn(project);

		Project projectFromMock = projectService.createOrUpdate(project);
		assertEquals(project.getId(), projectFromMock.getId());
	}

	@Test
	public void findAll() {
		List<Project> projects = TestUtil.buildProjectList();

		when(projectRepository.findAll()).thenReturn(projects);

		assertEquals(projects.size(), projectService.findAll().size());
		assertEquals(projects.get(0), projectService.findAll().get(0));
	}

	@Test
	public void findById() {
		String id = "opcdpofkfdfo";
		project.setId(id);

		when(projectRepository.findById(id)).thenReturn(Optional.of(project));
		Project projectById = projectService.findById(id).get();
		assertEquals(projectById.getId(), project.getId());
	}
}
