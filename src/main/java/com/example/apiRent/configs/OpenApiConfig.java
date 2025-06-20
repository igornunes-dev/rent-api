package com.example.api_rent_property.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Aluguel de Propriedades")
                        .version("v1") // A versão da sua API
                        .description("API para gerenciar proprietários, inquilinos, propriedades e contratos.")
                        .termsOfService("sem termos") // Opcional
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))); // Opcional
    }
}
