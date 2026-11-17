package com.isigntech.rightpath.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.isigntech.rightpath.entity.User;
import com.isigntech.rightpath.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Creates the initial admin account on first startup. Credentials come from the
 * ADMIN_EMAIL / ADMIN_PASSWORD environment variables (or the git-ignored .env file);
 * nothing is hard-coded and the password is never logged.
 */
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${app.admin.email:}")
	private String adminEmail;

	@Value("${app.admin.password:}")
	private String adminPassword;

	@Override
	public void run(String... args) {
		if (adminEmail.isBlank() || adminPassword.isBlank()) {
			log.warn("ADMIN_EMAIL / ADMIN_PASSWORD not set - skipping admin seeding");
			return;
		}

		String email = adminEmail.trim().toLowerCase();
		if (userRepository.existsByEmail(email)) {
			return;
		}

		userRepository.save(User.builder()
				.fullName("System Admin")
				.email(email)
				.passwordHash(passwordEncoder.encode(adminPassword))
				.role(User.Role.ADMIN)
				.build());
		log.info("Default admin account created: {}", email);
	}

}
