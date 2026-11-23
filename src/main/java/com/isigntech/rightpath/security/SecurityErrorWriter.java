package com.isigntech.rightpath.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletResponse;

/** Writes the same { status, message, timestamp } body as GlobalExceptionHandler, from the filter layer. */
final class SecurityErrorWriter {

	private SecurityErrorWriter() {
	}

	static void write(HttpServletResponse response, HttpStatus status, String message) throws IOException {
		response.setStatus(status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		// Messages are fixed strings without quotes, so no escaping is needed
		response.getWriter().write("{\"status\":" + status.value()
				+ ",\"message\":\"" + message + "\""
				+ ",\"timestamp\":\"" + LocalDateTime.now() + "\"}");
	}
}
