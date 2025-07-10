package com.suraj.currency.currency_conversion_service.service;

import com.suraj.currency.currency_conversion_service.response.CurrencyConversionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionService {

	private final CurrencyExchangeRateService currencyExchangeRateService;

	public CurrencyConversionResponse convertCurrency(String from, String to, double amount) {
		log.info("Converting {} from {} to {}", amount, from, to);
		if (from == null || to == null || from.trim().isEmpty() || to.trim().isEmpty()) {
			log.error("Currency codes cannot be null or empty");
			throw new IllegalArgumentException("Currency codes cannot be null or empty");
		}
		if (amount <= 0) {
			log.error("Amount must be greater than zero");
			throw new IllegalArgumentException("Amount must be greater than zero");
		}
		try {
			log.info("Fetching conversion rate from currency exchange service for {} to {}", from, to);
			// Fetching the conversion rate from a currency exchange service
			double conversionRateFromService = currencyExchangeRateService.getExchangeRate(from, to);
			log.info("Conversion rate from {} to {}: {}", from, to, conversionRateFromService);

			// Calculate the converted amount
			double convertedAmount = amount * conversionRateFromService;
			log.info("Converted amount: {}", convertedAmount);
			// Create and return the response
			return CurrencyConversionResponse.builder()
					.fromCurrency(from)
					.toCurrency(to)
					.amount(amount)
					.conversionRate(conversionRateFromService)
					.totalCalculatedAmount(convertedAmount)
					.build();
		} catch (Exception ex) {
			log.error("Error occurred while fetching: {}", ex.getMessage());
			throw new RuntimeException("Currency conversion failed: " + ex.getMessage());
		}

	}
}
