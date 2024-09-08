package com.example.budget.global.config;

import com.example.budget.domain.client.model.Role;
import com.example.budget.global.filter.JwtAuthenticationFilter;
import com.example.budget.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webConfigure() {
        return web -> web.ignoring()
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .disable();

        http
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/authenticate/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/budgets/**")).hasRole(Role.USER.name())
                                .requestMatchers(new AntPathRequestMatcher("/api/clients/**")).hasRole(Role.USER.name())
                                .requestMatchers(new AntPathRequestMatcher("/api/expenditures/**")).hasRole(Role.USER.name())
                                .anyRequest().authenticated());
        // H2 Console
        http
                .headers()
                .frameOptions()
                .sameOrigin();

        // Session 대신 JWT 사용
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
