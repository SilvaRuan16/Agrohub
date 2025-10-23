package br.com.agrohub.demo.config;

// No seu projeto Spring Boot (Backend)

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // Libera tudo abaixo de /api/v1/
            .allowedOrigins("http://localhost:3000", "http://localhost:5173") // Coloque a porta do seu front
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Libera os métodos
            .allowedHeaders("*") // Libera todos os cabeçalhos (como Content-Type)
            .allowCredentials(true);
    }
}