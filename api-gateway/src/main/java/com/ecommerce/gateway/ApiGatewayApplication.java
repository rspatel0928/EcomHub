package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("http://localhost:8081"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://localhost:8081"))
                
                // Product Service Routes
                .route("product-service", r -> r.path("/api/products/**")
                        .uri("http://localhost:8082"))
                
                .build();
    }
}
