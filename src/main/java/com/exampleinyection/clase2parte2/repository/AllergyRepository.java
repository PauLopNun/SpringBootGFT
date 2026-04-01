package com.exampleinyection.clase2parte2.repository;

import com.exampleinyection.clase2parte2.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {}
