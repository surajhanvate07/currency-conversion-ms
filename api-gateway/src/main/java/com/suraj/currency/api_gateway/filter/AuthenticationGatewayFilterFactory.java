package com.suraj.currency.api_gateway.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.suraj.currency.api_gateway.service.JwtService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

	private final JwtService jwtService;

	public AuthenticationGatewayFilterFactory(JwtService jwtService) {
		super(Config.class);
		this.jwtService = jwtService;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("Authentication filter Pre: {}", exchange.getRequest().getURI());
			if (!config.isEnabled()) {
				log.warn("Authentication filter is disabled, proceeding without authentication");
				return chain.filter(exchange);
			}

			String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				log.warn("Authorization header is missing or invalid");
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			String token = authorizationHeader.substring(7);

			Long userId = jwtService.extractUserIdFromToken(token);

			// Check if userId is null or invalid
			log.info("Extracted userId from token: {}", userId);
			if (userId == null || userId <= 0) {
				log.warn("Invalid token, userId could not be extracted");
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}

			log.info("Authenticated user with ID: {}", userId);
			ServerWebExchange mutatedExchange = exchange.mutate()
					.request(exchange.getRequest().mutate()
							.header("X-User-Id", String.valueOf(userId))
							.build())
					.build();

			return chain.filter(mutatedExchange);
		});
	}

	@Data
	public static class Config {
		private boolean enabled;
	}
}
