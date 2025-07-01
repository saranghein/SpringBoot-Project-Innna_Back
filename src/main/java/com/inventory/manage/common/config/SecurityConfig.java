package com.inventory.manage.common.config;


import com.inventory.manage.common.filter.JwtFilter;
import com.inventory.manage.common.filter.LoginFilter;
import com.inventory.manage.common.filter.LogoutFilter;
import com.inventory.manage.common.util.CookieGenerator;
import com.inventory.manage.common.util.JwtGenerator;
import com.inventory.manage.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CookieGenerator cookieGenerator;
    private final JwtGenerator jwtGenerator;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationConfiguration.getAuthenticationManager(),
                jwtGenerator,
                cookieGenerator);
        loginFilter.setFilterProcessesUrl("/login");
        http.csrf((csrf) -> csrf.disable())
                .formLogin((formLogin) -> formLogin.disable())
                .logout((logout) -> logout.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                .authorizeHttpRequests((authorizeRequest) -> authorizeRequest
                        .requestMatchers("/", "/login", "/signup", "/validate-id",  "/validate-nickname", "/reissue", "/exit",

                                // swagger 관련
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"

                        )
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(
                        loginFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtFilter(jwtUtil, userDetailsService), LoginFilter.class)
                .addFilterAfter(new LogoutFilter(jwtUtil,
                        cookieGenerator), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors((cors) -> cors.configurationSource(corsConfigSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();

                // 허용 경로 설정
//                configuration.addAllowedOrigin("https://localhost:8080");
//                configuration.addAllowedOrigin("https://localhost:5173");
                configuration.setAllowedOriginPatterns(List.of(
                        "https://localhost:8080",
                        "https://localhost:5173"
                ));
                configuration.addAllowedHeader("*");


                // Authorization 헤더를 응답에서 노출
                configuration.setExposedHeaders(List.of("Authorization"));

                // 허용 메서드 설정
                configuration.setAllowedMethods(
                        new ArrayList<>(List.of("GET", "POST", "PUT", "PATCH", "DELETE",
                                "OPTIONS")));

                // credentials 허용
                configuration.setAllowCredentials(true);

                return configuration;
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
