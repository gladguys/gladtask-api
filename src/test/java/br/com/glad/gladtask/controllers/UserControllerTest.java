package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.TestUtil;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.enums.ProfileEnum;
import br.com.glad.gladtask.security.jwt.JwtAuthenticationRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
public class UserControllerTest {

	MockMvc mockMvc;

	@Autowired
	MockMvc mockMvcAux;

	UserController userController;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	UserServiceImpl userService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userController = new UserController(userService, passwordEncoder);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	//@Test
	//public void create() throws Exception {
	//	String token = obtainAccessToken("dineresc@gmail.com","123456");
	//	User user = new User();
	//	user.setId("5bfadbf84c3aca3cf0f82b1c");
	//	user.setEmail("dededede@dedee.com");
//
//		MvcResult result = mockMvc.perform(post("/api/users")
//				.contentType("application/json")
//				.requestAttr("Authorization",token)
//				.content(new ObjectMapper().writeValueAsString(user))
//				.param("Authorization", token))
//				.andExpect(status().isOk())
//				.andReturn();
//	}

	@Test
	public void update() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");

		User user = new User();
		user.setEmail("teste@testes.com");
		user.setProfileEnum(ProfileEnum.ROLE_ADMIN);
		user.setId("5bfadbf84c3aca3cf0f82b1c");
		user.setPassword("testesteste");

		MvcResult result = mockMvc.perform(put("/api/users")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.content(new ObjectMapper().writeValueAsString(user))
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void findById() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");

		User user = new User();
		user.setEmail("teste@testes.com");
		user.setProfileEnum(ProfileEnum.ROLE_ADMIN);
		String id = "5bfadbf84c3aca3cf0f82b1c";
		user.setId(id);
		user.setPassword("testesteste");

		when(userService.findById(id)).thenReturn(user);

		MvcResult result = mockMvc.perform(get("/api/users/" + id).requestAttr("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();

		String content = result.getResponse().getContentAsString();

		assertTrue(content.contains("teste@testes.com"));
	}

	@Test
	public void delete() throws Exception {
		String userId = "5bfadbf84c3aca3cf0f82b1c";
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		User user = new User();
		user.setEmail("teste@testes.com");
		user.setProfileEnum(ProfileEnum.ROLE_ADMIN);

		when(userService.findById(userId)).thenReturn(user);
		doNothing().when(userService).delete(userId);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userId)
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void allUsersPaged() throws Exception {
		String token = obtainAccessToken("dineresc@gmail.com","123456");
		List<User> users = new ArrayList<>();
		Page<User> usersPaged = new PageImpl<>(users);

		when(userService.findAllPaged(0,10)).thenReturn(usersPaged);

		MvcResult result = mockMvc.perform(get("/api/users/0/10")
				.contentType("application/json")
				.requestAttr("Authorization",token)
				.param("Authorization", token))
				.andExpect(status().isOk())
				.andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
				.andReturn();
	}

	@Test
	public void allUsers() {
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
