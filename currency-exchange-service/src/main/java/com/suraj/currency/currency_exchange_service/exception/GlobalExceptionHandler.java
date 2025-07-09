package com.suraj.currency.currency_exchange_service.exception;

import com.suraj.currency.currency_exchange_service.response.ApiResponse;
import com.suraj.currency.currency_exchange_service.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiResponse response = new ApiResponse(
				ex.getLocalizedMessage(),
				new ErrorResponse(HttpStatus.NOT_FOUND.value())
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ApiResponse response = new ApiResponse(
				ex.getLocalizedMessage(),
				new ErrorResponse(HttpStatus.BAD_REQUEST.value())
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
		ApiResponse response = new ApiResponse(
				"An unexpected error occurred",
				new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value())
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
