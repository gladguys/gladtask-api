package br.com.glad.gladtask.controllers;

import br.com.glad.gladtask.entities.CurrentUser;
import br.com.glad.gladtask.entities.User;
import br.com.glad.gladtask.security.jwt.JwtAuthenticationRequest;
import br.com.glad.gladtask.security.jwt.JwtTokenUtil;
import br.com.glad.gladtask.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@Api(value = "Authenticaion", description = "Perform user authentication")
public class AuthenticationRestController {

	@Autowired private AuthenticationManager authenticationManager;

	@Autowired private JwtTokenUtil jwtTokenUtil;

	@Qualifier("jwtService")
	@Autowired private UserDetailsService userDetailsService;

	@Autowired private UserService  userService;

	@ApiOperation(value = "Perform user authentication and return user and his token")
	@PostMapping(value = "/api/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),
						authenticationRequest.getPassword()
				)
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final User user = userService.findByEmail(authenticationRequest.getEmail());
		user.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, user));
	}
}
