package br.com.glad.gladtask.services.impl;

import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

	UserServiceImpl userService;
	@Mock
	UserRepository userRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		userService = new UserServiceImpl(userRepository);
	}

	@Test
	public void findByEmail() {
	}

	@Test
	public void createOrUpdate() {
		User user = new User();
		String id = "2333dedefrgrtswe45fs";
		user.setId(id);

		when(userRepository.save(user)).thenReturn(user);

		User userMock = userService.createOrUpdate(user);
		assertEquals(user.getId(), userMock.getId());
	}

	@Test
	public void findById() {
		User user = new User();
		String id = "2333dedefrgrtswe45fs";

		when(userRepository.findById(id)).thenReturn(Optional.of(user));
		User userById = userService.findById(id);
		assertEquals(userById.getId(), user.getId());
	}

	@Test
	public void delete() {

		String userId = "dededeffvefbe435getg35";
		User userMocked = new User();
		userMocked.setId(userId);
		userMocked.setEmail("teste@teste.com");

		when(userRepository.findById(userId)).thenReturn(Optional.of(userMocked));

		doNothing().when(userRepository).delete(userMocked);
	}

	@Test
	public void findAllPaged() {
		List<User> users = new ArrayList<>();
		User user1 = new User();
		User user2 = new User();

		users.add(user1);
		users.add(user2);

		Page<User> usersPaged = new PageImpl<>(users);
		Pageable pageable = new PageRequest(0, 10);

		when(userRepository.findAll(pageable)).thenReturn(usersPaged);

		assertEquals(usersPaged.getTotalElements(), userService.findAllPaged(0,10).getTotalElements());
	}

	@Test
	public void findAll() {
		List<User> users = new ArrayList<>();
		User user1 = new User();
		User user2 = new User();
		users.add(user1);
		users.add(user2);

		when(userRepository.findAll()).thenReturn(users);

		assertEquals(users.size(), userService.findAll().size());
		assertEquals(users.get(0), userService.findAll().get(0));
	}
}