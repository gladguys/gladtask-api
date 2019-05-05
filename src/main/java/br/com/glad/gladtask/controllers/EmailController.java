package br.com.glad.gladtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/email")
@CrossOrigin( origins = "*")
@Api(value = "Email Controller", description = "Perform operations regard email sends")
public class EmailController {

	@Autowired private JavaMailSender mailSender;

	@PostMapping(value = "/inviteTeam//{email:.+}")
	public ResponseEntity<String> sendInviteToTeamEmail(@PathVariable("email") String email, @RequestBody String url) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("Convite para o Gladtask!");
		message.setText("Você foi convidado para ser um Glad. Acesse o link para criar uma conta " + url);
		message.setTo(email);
		message.setFrom("gladtask@gmail.com");

		try {
			mailSender.send(message);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
