package com.example.UserManagementService.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("=== REGISTERING ORDER(2) SECURITY CHAIN ===");
        http
                .securityMatcher(request -> {
                    String uri = request.getRequestURI();
                    boolean matches = uri.startsWith("/auth");
                    System.out.println("Order(2) SecurityMatcher - URI: " + uri + ", matches: " + matches);
                    return matches;
                })
                .authorizeHttpRequests(requests ->{
                            System.out.println("=== REGISTERING ORDER(2) SECURITY CHAIN ===");
                            requests.anyRequest().permitAll();
                        })
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
