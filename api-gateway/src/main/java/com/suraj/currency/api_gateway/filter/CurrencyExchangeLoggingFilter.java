package com.suraj.currency.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CurrencyExchangeLoggingFilter extends AbstractGatewayFilterFactory<CurrencyExchangeLoggingFilter.Config> {

	public CurrencyExchangeLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("Currency Exchange filter Pre: {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("Exception in Currency Exchange filter", error))
					.then(Mono.fromRunnable(() -> {
						log.info("Currency Exchange filter Post: Response Status: {}", exchange.getResponse().getStatusCode());
					}));
		});
	}

	public static class Config {

	}
}
