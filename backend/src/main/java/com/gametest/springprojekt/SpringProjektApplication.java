package com.gametest.springprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringProjektApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjektApplication.class, args);
    }

}
