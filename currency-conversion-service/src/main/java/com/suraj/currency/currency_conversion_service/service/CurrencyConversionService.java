package com.suraj.currency.currency_conversion_service.service;

import com.suraj.currency.currency_conversion_service.client.CurrencyExchangeFeignClient;
import com.suraj.currency.currency_conversion_service.exception.CurrencyPairNotFoundException;
import com.suraj.currency.currency_conversion_service.response.CurrencyConversionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import feign.FeignException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionService {

	private final CurrencyExchangeFeignClient currencyExchangeFeignClient;

	public CurrencyConversionResponse convertCurrency(String from, String to, double amount) {
		log.info("Converting {} from {} to {}", amount, from, to);

		try {
			log.info("Fetching conversion rate from currency exchange service for {} to {}", from, to);
			// Fetching the conversion rate from a currency exchange service
			double conversionRateFromService = currencyExchangeFeignClient.getExchangeRate(from, to);
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
		} catch (FeignException.NotFound ex) {
			log.error("Currency pair not found: {} to {}", from, to);
			throw new CurrencyPairNotFoundException("Currency pair not found for " + from + " to " + to);
		} catch (FeignException ex) {
			log.error("Error fetching conversion rate: {}", ex.getMessage());
			throw new RuntimeException("Currency conversion failed: " + ex.getMessage());
		}

	}
}
