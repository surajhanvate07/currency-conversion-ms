package com.suraj.currency.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalLoggingFilter implements GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Global Logging Filter Pre: Request URI: {}", exchange.getRequest().getURI());

		return chain.filter(exchange)
				.doOnError(error -> log.error("Exception in downstream processing", error))
				.then(Mono.fromRunnable(() -> {
					log.info("Global Logging Filter Post: Response Status: {}", exchange.getResponse().getStatusCode());
				}));
	}
}
