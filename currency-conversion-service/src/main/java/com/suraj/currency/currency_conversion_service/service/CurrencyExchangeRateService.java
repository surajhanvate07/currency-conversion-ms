package com.suraj.currency.currency_conversion_service.service;

import com.suraj.currency.currency_conversion_service.client.CurrencyExchangeFeignClient;
import com.suraj.currency.currency_conversion_service.exception.CurrencyPairNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateService {

	private final CurrencyExchangeFeignClient currencyExchangeFeignClient;

	@Cacheable(value = "exchangeRates", key = "#from + ':' + #to")
	public double getExchangeRate(String from, String to) {
		log.info("Fetching exchange rate CurrencyExchangeFeignClient");
		try {
			double exchangeRate = currencyExchangeFeignClient.getExchangeRate(from, to);
			log.info("Exchange rate from {} to {}: {}", from, to, exchangeRate);
			return exchangeRate;
		} catch (FeignException.NotFound ex) {
			log.error("Currency pair not found: {} to {}", from, to);
			throw new CurrencyPairNotFoundException("Currency pair not found for " + from + " to " + to);
		} catch (FeignException ex) {
			log.error("Error fetching conversion rate: {}", ex.getMessage());
			throw new RuntimeException("Currency conversion failed: " + ex.getMessage());
		}
	}
}
