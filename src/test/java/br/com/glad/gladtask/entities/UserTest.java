package br.com.glad.gladtask.entities;

import br.com.glad.gladtask.entities.enums.ProfileEnum;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

	User user;

	@Before
	public void setUp() {
		user = new User();
	}

	@Test
	public void getId() {
		String id = "1233fhdsk3r443d";
		user.setId(id);

		assertEquals(id, user.getId());
	}

	@Test
	public void getEmail() {
		String email = "dedede@gmail.com";
		user.setEmail(email);

		assertEquals(email, user.getEmail());
	}

	@Test
	public void getProfileEnum() {

		ProfileEnum pe = ProfileEnum.ROLE_ADMIN;
		user.setProfileEnum(pe);

		assertEquals(pe, user.getProfileEnum());
	}
}