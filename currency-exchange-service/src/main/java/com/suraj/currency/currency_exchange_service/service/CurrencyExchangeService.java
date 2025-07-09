package com.suraj.currency.currency_exchange_service.service;

import com.suraj.currency.currency_exchange_service.entity.ExchangeRate;
import com.suraj.currency.currency_exchange_service.exception.ResourceNotFoundException;
import com.suraj.currency.currency_exchange_service.repository.CurrencyExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangeService {

	private final CurrencyExchangeRepository currencyExchangeRepository;

	// Method to get the exchange rate between two currencies
	public double getExchangeRate(String fromCurrency, String toCurrency) {
		log.info("Fetching exchange rate from {} to {}", fromCurrency, toCurrency);
		if (fromCurrency == null || toCurrency == null || fromCurrency.trim().isEmpty() || toCurrency.trim().isEmpty()) {
			log.error("Currency codes cannot be null");
			throw new IllegalArgumentException("Currency codes cannot be null or empty");
		}

		if (!isCurrencyPairExists(fromCurrency, toCurrency)) {
			log.error("Currency pair {} to {} does not exist", fromCurrency, toCurrency);
			throw new ResourceNotFoundException("Currency pair does not exist for " + fromCurrency + " to " + toCurrency);
		}
		ExchangeRate exchangeRate = currencyExchangeRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency)
				.orElseThrow(() -> new ResourceNotFoundException("Currency pair not found for " + fromCurrency + " to " + toCurrency));

		log.info("Exchange rate found: {} to {} = {}", fromCurrency, toCurrency, exchangeRate.getConversionRate());
		return exchangeRate.getConversionRate();
	}

	private boolean isCurrencyPairExists(String fromCurrency, String toCurrency) {
		log.info("Checking if currency pair exists: {} to {}", fromCurrency, toCurrency);
		return currencyExchangeRepository.existsByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
	}
}
