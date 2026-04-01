package com.exampleinyection.clase2parte2.dto;

import java.util.List;

public record UserDTO(Long id, String name, int age, List<AllergyDTO> allergies) {}
