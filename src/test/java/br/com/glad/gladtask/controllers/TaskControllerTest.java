package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.TestUtil;
import br.com.glad.gladtask.entities.Task;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.enums.StatusEnum;
import br.com.glad.gladtask.security.jwt.JwtAuthenticationRequest;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.impl.TaskServiceImpl;
import br.com.glad.gladtask.services.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

	MockMvc mockMvc;

	@Autowired MockMvc mockMvcAux;

	@Mock TaskServiceImpl taskServiceMock;
	@Mock JwtTokenUtil jwtTokenUtil;
	@Mock UserServiceImpl userServiceMock;

	TaskController taskController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		taskController = new TaskController(taskServiceMock, userServiceMock, jwtTokenUtil);
		this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
	}

	@Test
	public void create() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		Task task = new Task();
		String title = "a task test";
		task.setStatus(StatusEnum.CRIADA);

		User user = new User();
		user.setEmail("dineresc@gmail.com");

		task.setTitle(title);
		when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("dineresc@gmail.com");
		when(userServiceMock.findByEmail("dineresc@gmail.com")).thenReturn(user);
		when(taskServiceMock.createOrUpdate(task)).thenReturn(task);

		mockMvc.perform(post("/api/tasks")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.header("Authorization",token)
				.content(new ObjectMapper().writeValueAsString(task))
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void update() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		Task task = new Task();
		String title = "a task test";
		task.setTitle(title);
		task.setStatus(StatusEnum.CONCLUIDA);
		task.setTaskChanges(new ArrayList<>());

		User user = new User();
		user.setEmail("dineresc@gmail.com");

		when(jwtTokenUtil.getUsernameFromToken(token)).thenReturn("dineresc@gmail.com");
		when(userServiceMock.findByEmail("dineresc@gmail.com")).thenReturn(user);
		when(taskServiceMock.createOrUpdate(task)).thenReturn(task);

		mockMvc.perform(put("/api/tasks")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.header("Authorization",token)
				.content(new ObjectMapper().writeValueAsString(task))
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void findAllPaged() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		List<Task> tasks = TestUtil.montaListaTasks();
		Page<Task> tasksPaged = new PageImpl<>(tasks);

		when(taskServiceMock.findAll(0,10)).thenReturn(tasksPaged);

		mockMvc.perform(get("/api/tasks/0/10")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void findAll() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		List<Task> tasks = TestUtil.montaListaTasks();


		when(taskServiceMock.findAll()).thenReturn(tasks);

		mockMvc.perform(get("/api/tasks")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void findTasksByTargetUser() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		List<Task> tasks = TestUtil.montaListaTasks();
		String userId = "5bfadbf84c3aca3cf0f82b1c";

		when(taskServiceMock.findByTargetUser(userId)).thenReturn(tasks);

		mockMvc.perform(get("/api/tasks/user-creator/" + userId)
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andReturn();
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