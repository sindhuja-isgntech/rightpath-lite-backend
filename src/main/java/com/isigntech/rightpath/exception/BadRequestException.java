package com.isigntech.rightpath.exception;

/** Maps to HTTP 400. */
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
