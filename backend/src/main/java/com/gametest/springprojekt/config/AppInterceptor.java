package com.gametest.springprojekt.config;

import com.gametest.springprojekt.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class AppInterceptor implements HandlerInterceptor {
    private final UserService userService;


    public AppInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String uri = request.getRequestURI();

        if (uri.startsWith("/assets") || uri.startsWith("/thymeleaf") || uri.startsWith("/api") || uri.startsWith("/logout") || uri.equals("/error")) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !Objects.equals(auth.getPrincipal(), "anonymousUser");

        if (isAuthenticated) {
            if (uri.startsWith("/mvc/admin")) {//nie wymuszamy na adminie tworzenia postaci
                return true;
            }

            String username = auth.getName();
            boolean hasCharacter = userService.hasCharacter(username);

            if (uri.equals("/mvc/login") || uri.equals("/mvc/register")) {
                response.sendRedirect(hasCharacter ? "/" : "/mvc/character-creator");
                return false;
            }

            if (!hasCharacter && !uri.equals("/mvc/character-creator")) {
                response.sendRedirect("/mvc/character-creator");
                return false;
            }

            if (hasCharacter && uri.equals("/mvc/character-creator")) {
                response.sendRedirect("/");
                return false;
            }
        }
        return true;
    }
}
