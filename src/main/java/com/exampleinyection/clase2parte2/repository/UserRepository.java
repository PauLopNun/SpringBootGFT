package com.exampleinyection.clase2parte2.repository;

import com.exampleinyection.clase2parte2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //List<User> findByName(String name);
}
