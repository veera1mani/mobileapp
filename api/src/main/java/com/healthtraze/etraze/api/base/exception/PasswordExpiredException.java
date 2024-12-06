package com.healthtraze.etraze.api.base.exception;

public class PasswordExpiredException extends RuntimeException {
	private static final long serialVersionUID = 5861310937366287163L;

	public PasswordExpiredException() {
		super();
	}

	public PasswordExpiredException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PasswordExpiredException(final String message) {
		super(message);
	}

	public PasswordExpiredException(final Throwable cause) {
		super(cause);
	}
}
