package com.gametest.springprojekt.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/mvc/login", "/mvc/register", "/index.html", "/mvc/character-creator", "/assets/**", "/thymeleaf/**").permitAll() //zezwolenie do logowania i plików statycznych
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/login") //musi być bo inny endpoint niż domyślny
                        .defaultSuccessUrl("/") //bez true bo nie chcemy wymuszać przekierowania
                        .permitAll())
                .csrf(csrf -> csrf.disable()) // USUNĄĆ NA KONICEC, TO JEST TYLKO NA POTRZEBY TESTOWANIA Z POSTMANEM!!!!!!!!!!!!!!!!!!!!!!!!
                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Brak autoryzacji - musisz sie zalogowac przez Basic Auth"); //cały if do wysyłania komunikatu gdy ktoś chce skorzystać z api bez autoryzacji
                    } else {
                        response.sendRedirect("/login");
                    }
                }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/mvc/login?logout") //parametr do wyświetlenia komunikatu o wylogowaniu
                )
                .headers(headers -> headers.frameOptions(f -> f.disable()));;
        return http.build();
    }


}
