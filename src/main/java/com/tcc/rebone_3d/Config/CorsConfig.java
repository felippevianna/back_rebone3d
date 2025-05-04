package com.tcc.rebone_3d.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Permite CORS para todas as rotas sob /api
                        .allowedOrigins("http://localhost:3000", "https://reboned3d.netlify.app") // Permite requisições do frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Headers permitidos
                        .exposedHeaders("Content-Disposition") // <-- Adicione esta linha
                        .allowCredentials(true); // Permite credenciais (cookies, tokens)
            }
        };
    }
}