package com.ecommerce.gateway.config;

import com.ecommerce.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Public routes (no authentication required)
                .route("auth-login", r -> r.path("/api/auth/login")
                        .uri("http://localhost:8081"))
                .route("auth-register", r -> r.path("/api/auth/register")
                        .uri("http://localhost:8081"))
                
                // Protected User Service Routes
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8081"))
                
                // Protected Product Service Routes
                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))
                
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000"); // React app URL
        corsConfig.addAllowedOrigin("http://localhost:4200"); // Angular app URL
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
