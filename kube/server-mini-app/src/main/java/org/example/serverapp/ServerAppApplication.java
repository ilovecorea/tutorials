package org.example.serverapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.serverapp.repository")
public class ServerAppApplication {
	public static void main(String... args) {
		SpringApplication.run(ServerAppApplication.class, args);
	}
}
