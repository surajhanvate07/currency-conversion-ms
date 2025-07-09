package com.suraj.currency.currency_exchange_service.controller;

import com.suraj.currency.currency_exchange_service.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency-exchange")
@RequiredArgsConstructor
public class CurrencyExchangeController {

	private final CurrencyExchangeService currencyExchangeService;

	// Endpoint to get the exchange rate between two currencies
	@GetMapping("/from/{fromCurrency}/to/{toCurrency}/rate")
	public ResponseEntity<Double> getExchangeRate(@PathVariable("fromCurrency") String from, @PathVariable("toCurrency") String to) {
		double exchangeRate = currencyExchangeService.getExchangeRate(from, to);
		return ResponseEntity.ok(exchangeRate);
	}
}
