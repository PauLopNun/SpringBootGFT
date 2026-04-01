package com.exampleinyection.clase2parte2.repository;

import com.exampleinyection.clase2parte2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.allergies")
    List<User> findAllWithAllergies();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    int updateNameById(@Param("id") Long id, @Param("name") String name);
}
