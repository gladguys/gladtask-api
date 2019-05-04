package br.com.glad.gladtask.security.jwt;

import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.entities.enums.ProfileEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUserFactory {

	private JwtUserFactory() {

	}

	public static JwtUser create(User user) {
		return new JwtUser( user.getId(),
				user.getEmail(),
				user.getPassword(),
				mapToGrantedAuthorities(user.getProfileEnum())
		);
	}

	private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profile) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profile.toString()));
		return authorities;
	}
}