package com.suraj.currency.api_gateway.filter;

import com.suraj.currency.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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

			Long userId = null;
			try {
				userId = jwtService.extractUserIdFromToken(token);
			} catch (JwtException | IllegalArgumentException e) {
				return unauthorized(exchange.getResponse(), "Invalid or expired JWT token");
			}

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

			log.info("Authentication filter Post: User ID added to request headers");
			return chain.filter(mutatedExchange);
		});
	}

	private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add("Content-Type", "application/json");
		byte[] bytes = ("{\"error\": \"" + message + "\"}").getBytes();
		return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
	}

	@Data
	public static class Config {
		private boolean enabled;
	}
}
