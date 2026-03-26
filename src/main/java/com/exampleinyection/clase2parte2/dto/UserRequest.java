package com.exampleinyection.clase2parte2.dto;

import com.exampleinyection.clase2parte2.model.Allergy;
import java.util.List;

public record UserRequest(
        String name,
        Integer age,    
        List<Allergy> allergies
) {}