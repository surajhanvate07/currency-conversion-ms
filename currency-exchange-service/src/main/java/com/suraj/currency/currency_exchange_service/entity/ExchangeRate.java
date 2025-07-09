package com.suraj.currency.currency_exchange_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exchange_rate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRate {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	private String fromCurrency;
	private String toCurrency;
	private double conversionRate;
}
