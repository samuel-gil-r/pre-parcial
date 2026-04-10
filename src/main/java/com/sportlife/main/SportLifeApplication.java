package com.sportlife.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación ECI-SportLife.
 */
@SpringBootApplication(scanBasePackages = "com.sportlife")
public class SportLifeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportLifeApplication.class, args);
    }
}
