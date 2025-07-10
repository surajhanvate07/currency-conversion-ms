package com.suraj.currency.currency_conversion_service.service;

import com.suraj.currency.currency_conversion_service.response.CurrencyConversionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CurrencyConversionService {
	public CurrencyConversionResponse convertCurrency(String from, String to, double amount) {

		log.info("Converting {} from {} to {}", amount, from, to);
		// Simulate a conversion rate for demonstration purposes
		double conversionRate = 82.5; // Example conversion rate for USD to INR
		// Fetching the conversion rate from a currency exchange service
		if("USD".equalsIgnoreCase(from) && "INR".equalsIgnoreCase(to)) {
			double convertedAmount = amount * conversionRate;
			log.info("Converted amount: {}", convertedAmount);
			// Create and return the response
			return CurrencyConversionResponse.builder()
					.fromCurrency(from)
					.toCurrency(to)
					.amount(amount)
					.conversionRate(conversionRate)
					.totalCalculatedAmount(convertedAmount)
					.build();
		} else {
			log.error("Unsupported currency conversion from {} to {}", from, to);
			throw new UnsupportedOperationException("Currency conversion not supported for " + from + " to " + to);
		}
	}
}
