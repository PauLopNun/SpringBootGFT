package com.exampleinyection.clase2parte2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class GFTFormacion {

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        SpringApplication.run(GFTFormacion.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            log.info("App name: {}", appName);
        };
    }
}