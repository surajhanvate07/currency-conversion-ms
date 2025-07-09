package com.suraj.currency.currency_exchange_service.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

	private int statusCode;
	private LocalDateTime timestamp;

	public ErrorResponse(int statusCode) {
		this.statusCode = statusCode;
		this.timestamp = LocalDateTime.now();
	}
}
