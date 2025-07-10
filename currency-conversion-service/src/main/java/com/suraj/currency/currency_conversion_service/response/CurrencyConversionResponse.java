package com.suraj.currency.currency_conversion_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyConversionResponse {

	private String fromCurrency;
	private String toCurrency;
	private double amount;
	private double conversionRate;
	private double totalCalculatedAmount;
}
