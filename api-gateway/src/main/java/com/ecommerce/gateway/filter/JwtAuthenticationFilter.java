package com.ecommerce.gateway.filter;

import com.ecommerce.gateway.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            if (!containsAuthorization(request)) {
                return onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            } else {
                return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }

            try {
                if (!jwtTokenUtil.validateToken(token)) {
                    return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
                }

                // Add user info to request headers
                String username = jwtTokenUtil.getUsernameFromToken(token);
                String roles = jwtTokenUtil.getRolesFromToken(token);

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Roles", roles)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                return onError(exchange, "JWT token validation failed", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = "{\"error\": \"" + err + "\", \"status\": " + httpStatus.value() + "}";
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}
