package com.suraj.currency.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.CONFLICT);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(InvalidCredentialException.class)
	public ResponseEntity<ApiError> handleInvalidCredentialException(InvalidCredentialException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
		ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}
}
