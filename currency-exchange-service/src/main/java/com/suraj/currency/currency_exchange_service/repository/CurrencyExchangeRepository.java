package com.suraj.currency.currency_exchange_service.repository;

import com.suraj.currency.currency_exchange_service.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyExchangeRepository extends JpaRepository<ExchangeRate, Long> {

	boolean existsByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);

	Optional<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);
}
