package com.gametest.springprojekt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppInterceptor appInterceptor;

    public WebConfig(AppInterceptor appInterceptor) {
        this.appInterceptor = appInterceptor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //przekierowanie ścieżki bez kropki(nie plików) i nie z /api/ ani /mvc/ do index.html
        registry.addViewController("/{path:^(?!api|mvc)[^.]+$}")
                .setViewName("forward:/index.html");
        //Strona główna to samo
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor);
    }
}