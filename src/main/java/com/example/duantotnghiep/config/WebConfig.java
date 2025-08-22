package com.example.duantotnghiep.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(System.getProperty("user.dir"), "uploads").toString();
        
        // Map URL /uploads/** to the physical uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
                
        System.out.println("=== DEBUG: Cấu hình ResourceHandler ===");
        System.out.println("Upload path: " + uploadPath);
        System.out.println("URL pattern: /uploads/**");
        System.out.println("====================================");
    }
}