package com.suraj.currency.currency_conversion_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange-service", path = "/currency-exchange")
public interface CurrencyExchangeFeignClient {

	@GetMapping("/from/{fromCurrency}/to/{toCurrency}/rate")
	Double getExchangeRate(@PathVariable("fromCurrency") String fromCurrency,
						   @PathVariable("toCurrency") String toCurrency);
}
