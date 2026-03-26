package com.exampleinyection.clase2parte2.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(0) @Max(120)
    private int age;

    @Valid
    private List<Allergy> allergies;
}