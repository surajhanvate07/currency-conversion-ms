package com.suraj.currency.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserLoggingFilter extends AbstractGatewayFilterFactory<UserLoggingFilter.Config> {

	public UserLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("User filter Pre: {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("Exception in User filter", error))
					.then(Mono.fromRunnable(() -> {
						log.info("User filter Post: Response Status: {}", exchange.getResponse().getStatusCode());
					}));
		});
	}

	public static class Config {

	}
}
