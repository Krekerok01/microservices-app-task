package com.specificgroup.user.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Provides global application configuration
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public Hibernate5Module hibernateModule() {
        return new Hibernate5Module();
    }
}