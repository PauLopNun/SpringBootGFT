package com.exampleinyection.clase2parte2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int severity;

    @ManyToOne
    private User user;
    
    public Allergy toDTO(Allergy allergy) {
        if (allergy == null) {
            return null;
        }

        Allergy dto = new Allergy();
        dto.setId(allergy.getId());
        dto.setName(allergy.getName());
        dto.setSeverity(allergy.getSeverity());
        return dto;
    }
}