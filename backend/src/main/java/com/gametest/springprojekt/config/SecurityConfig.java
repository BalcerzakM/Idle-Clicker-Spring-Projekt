package com.gametest.springprojekt.config;

import com.gametest.springprojekt.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity//do zabezpieczeń przed metodami
public class SecurityConfig {
    private final LoginSuccessHandler successHandler;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(LoginSuccessHandler successHandler, CustomUserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/mvc/login", "/mvc/register", "/thymeleaf/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()//zezwolenie do logowania
                        .requestMatchers("/mvc/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/login") //musi być bo inny endpoint niż domyślny
                        .successHandler(successHandler)
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("rememberMe")
                        .key("SrubaRpgBardzoTajnyKlucz2026!@#")
                        .tokenValiditySeconds(7*24*60*60)
                        .userDetailsService(userDetailsService)
                )
                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Brak autoryzacji - musisz sie zalogowac przez Basic Auth"); //cały if do wysyłania komunikatu gdy ktoś chce skorzystać z api bez autoryzacji
                    } else {
                        response.sendRedirect("/mvc/login");
                    }
                }))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/mvc/login?logout") //parametr do wyświetlenia komunikatu o wylogowaniu
                        .deleteCookies("remember-me")
                )
                .headers(headers -> headers.frameOptions(f -> f.disable()));
        return http.build();
    }


}
