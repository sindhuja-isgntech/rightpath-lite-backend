package com.isigntech.rightpath.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Returns 403 for authenticated users who lack the required role.
 *
 * It writes the response directly instead of using response.sendError(): sendError makes the
 * container re-dispatch to /error, and that internal request carries no JWT, so security would
 * answer it with 401 and hide the real 403.
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		SecurityErrorWriter.write(response, HttpStatus.FORBIDDEN,
				"You do not have permission to perform this action");
	}
}
