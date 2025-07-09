package com.suraj.currency.currency_conversion_service.controller;

import com.suraj.currency.currency_conversion_service.response.CurrencyConversionResponse;
import com.suraj.currency.currency_conversion_service.service.CurrencyConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

	@Autowired
	private CurrencyConversionService currencyConversionService;

	@GetMapping("/from/{fromCurrency}/to/{toCurrency}/amount/{amount}")
	public ResponseEntity<CurrencyConversionResponse> convertCurrency(@PathVariable("fromCurrency") String from,
																	   @PathVariable("toCurrency") String to,
																	   @PathVariable("amount") double amount) {
		CurrencyConversionResponse response = currencyConversionService.convertCurrency(from, to, amount);
		return ResponseEntity.ok(response);

	}
}
