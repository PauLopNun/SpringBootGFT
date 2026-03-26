package com.exampleinyection.clase2parte2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    private String name;
    private UpdateSettings update;
    private PaginationSettings pagination;
    private DefaultSettings defaults;

    @Getter @Setter
    public static class UpdateSettings {
        private boolean disabled;
        private String message;
    }

    @Getter @Setter
    public static class PaginationSettings {
        private int maxSize;
    }

    @Getter @Setter
    public static class DefaultSettings {
        private String name;
        private int age;
    }
}