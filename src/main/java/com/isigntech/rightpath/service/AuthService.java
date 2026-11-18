package com.isigntech.rightpath.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isigntech.rightpath.dto.AuthResponse;
import com.isigntech.rightpath.dto.LoginRequest;
import com.isigntech.rightpath.dto.RegisterRequest;
import com.isigntech.rightpath.dto.UserResponse;
import com.isigntech.rightpath.entity.User;
import com.isigntech.rightpath.exception.ConflictException;
import com.isigntech.rightpath.exception.ResourceNotFoundException;
import com.isigntech.rightpath.mapper.DtoMapper;
import com.isigntech.rightpath.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	/** Public sign-up always creates a CANDIDATE; admins are created by the seeder only. */
	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = normalize(request.email());

		if (userRepository.existsByEmail(email)) {
			throw new ConflictException("An account with this email already exists");
		}

		User user = userRepository.save(User.builder()
				.fullName(request.firstName().trim() + " " + request.lastName().trim())
				.email(email)
				.mobileNumber(request.mobileNumber())
				.passwordHash(passwordEncoder.encode(request.password()))
				.role(User.Role.CANDIDATE)
				.build());

		return authenticate(email, request.password(), user);
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		String email = normalize(request.email());
		// Throws AuthenticationException on bad credentials (handled as 401)
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, request.password()));

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return new AuthResponse(jwtService.generateToken(authentication), DtoMapper.toResponse(user));
	}

	@Transactional(readOnly = true)
	public UserResponse getCurrentUser(String email) {
		return userRepository.findByEmail(email)
				.map(DtoMapper::toResponse)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private AuthResponse authenticate(String email, String rawPassword, User user) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, rawPassword));
		return new AuthResponse(jwtService.generateToken(authentication), DtoMapper.toResponse(user));
	}

	private String normalize(String email) {
		return email.trim().toLowerCase();
	}
}
