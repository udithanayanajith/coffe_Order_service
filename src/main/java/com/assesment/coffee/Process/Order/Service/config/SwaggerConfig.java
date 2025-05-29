package com.assesment.coffee.Process.Order.Service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coffee Order Processing Service")
                        .description("API for processing coffee orders - Assessment Part 2")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Candidate")
                                .email("candidate@example.com")));
    }
}