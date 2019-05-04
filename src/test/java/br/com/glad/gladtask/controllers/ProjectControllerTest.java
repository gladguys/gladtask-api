package br.com.glad.gladtask.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.glad.gladtask.services.impl.TaskServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.glad.gladtask.TestUtil;
import br.com.glad.gladtask.entities.Project;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.security.jwt.JwtAuthenticationRequest;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.impl.ProjectServiceImpl;
import br.com.glad.gladtask.services.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

	MockMvc mockMvc;

	@Autowired MockMvc mockMvcAux;

	@Mock ProjectServiceImpl projectServiceMock;
	@Mock JwtTokenUtil jwtTokenUtil;
	@Mock UserServiceImpl userServiceMock;
	@Mock TaskServiceImpl taskServiceMock;

	Project project;
	ProjectController projectController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		projectController = new ProjectController(projectServiceMock, taskServiceMock, userServiceMock, jwtTokenUtil);
		this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
		this.project = new Project();
	}

	@Test
	public void create() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		String name = "p name";
		Project project = new Project();
		project.setName(name);

		User user = new User();
		user.setEmail("dineresc@gmail.com");

		when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("dineresc@gmail.com");
		when(userServiceMock.findByEmail("dineresc@gmail.com")).thenReturn(user);
		when(projectServiceMock.createOrUpdate(project)).thenReturn(project);

		mockMvc.perform(post("/api/project")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.header("Authorization",token)
				.content(new ObjectMapper().writeValueAsString(project))
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void update() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		String name = "p name";
		String projectId = "934i2349023402";
		Project project = new Project();
		project.setId(projectId);
		project.setName(name);

		User user = new User();
		user.setEmail("dineresc@gmail.com");

		when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("dineresc@gmail.com");
		when(userServiceMock.findByEmail("dineresc@gmail.com")).thenReturn(user);
		when(projectServiceMock.createOrUpdate(project)).thenReturn(project);

		mockMvc.perform(put("/api/project")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.header("Authorization",token)
				.content(new ObjectMapper().writeValueAsString(project))
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void findAll() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		String id = "5c479d4f1e87b91090e2fa72";
		String name = "Sample Project";
		project.setName(name);
		project.setId(id);
		Project project2 = new Project();
		List<Project> projects = new ArrayList<>();
		projects.add(project);
		projects.add(project2);

		when(projectServiceMock.findAll()).thenReturn(projects);

		mockMvc.perform(get("/api/project/")
				.requestAttr("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.*", hasSize(2)))
				.andReturn();
	}

	@Test
	public void findById() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		String id = "5c479d4f1e87b91090e2fa72";
		String name = "Sample Project";
		project.setName(name);
		project.setId(id);

		when(projectServiceMock.findById(id)).thenReturn(Optional.of(project));

		MvcResult result = mockMvc.perform(get("/api/project/" + id)
				.requestAttr("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		assertTrue(content.contains(name));
	}

	private String obtainAccessToken(String username, String password) throws Exception {
		JacksonJsonParser jsonParser = new JacksonJsonParser();

		JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest();
		jwtAuthenticationRequest.setEmail("dineresc@gmail.com");
		jwtAuthenticationRequest.setPassword("123456");

		ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = objectWriter.writeValueAsString(jwtAuthenticationRequest);

		ResultActions result
				= mockMvcAux.perform(post("/api/auth")
				.contentType("application/json")
				.content(json)
				.accept("application/json;charset=UTF-8"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));

		String resultString = result.andReturn().getResponse().getContentAsString();

		return jsonParser.parseMap(resultString).get("token").toString();
	}
}
