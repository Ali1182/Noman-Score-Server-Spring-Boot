package com.server_ns.nomad_score.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "https://nomad-score.com", "https://www.nomad-score.com", "https://lovable.dev/projects/d4a740c1-aae6-4199-a270-22048fa20e49", "https://d4a740c1-aae6-4199-a270-22048fa20e49.lovableproject.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
