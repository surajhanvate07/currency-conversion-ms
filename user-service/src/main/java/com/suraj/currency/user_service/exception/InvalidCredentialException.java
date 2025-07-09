package com.suraj.currency.user_service.exception;

public class InvalidCredentialException extends RuntimeException{
	public InvalidCredentialException(String message) {
		super(message);
	}
}
