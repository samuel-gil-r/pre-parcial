package com.sportlife.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de SpringDoc OpenAPI para la documentación Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ECI-SportLife API")
                        .version("1.0.0")
                        .description("API REST para tienda deportiva virtual ECI-SportLife")
                        .contact(new Contact()
                                .name("ECI Dev Team")
                                .email("dev@sportlife.com")));
    }
}
