package com.exampleinyection.clase2parte2.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app")
@Validated
@Getter
@Setter
public class AppConfig {

    @NotBlank(message = "El nombre de la app no puede estar vacío")
    private String name;

    @NotNull @Valid
    private UpdateSettings update;

    @NotNull @Valid
    private PaginationSettings pagination;

    @NotNull @Valid
    private DefaultSettings defaults;

    @Getter @Setter
    public static class UpdateSettings {
        private boolean disabled;
        @NotBlank
        private String message;
    }

    @Getter @Setter
    public static class PaginationSettings {
        @Min(1)
        private int maxSize;
    }

    @Getter @Setter
    public static class DefaultSettings {
        @NotBlank
        private String name;
        @Min(0)
        private int age;
    }
}