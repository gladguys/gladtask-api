package br.com.glad.gladtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class GladtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(GladtaskApplication.class, args);
	}
}
