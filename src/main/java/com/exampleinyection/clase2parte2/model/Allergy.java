package com.exampleinyection.clase2parte2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToMany(mappedBy = "allergies")
    @JsonIgnore
    private List<User> users;

    public Allergy(String name, int severity) {
        this.name = name;
        this.severity = severity;
    }
}
