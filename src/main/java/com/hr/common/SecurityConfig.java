//package com.hr.common;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .cors(Customizer.withDefaults())
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(authz -> authz
//                .requestMatchers("/api/**").permitAll()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/index.html").permitAll()
//                .anyRequest().permitAll()
//            );
//        
//        return http.build();
//    }
//}
