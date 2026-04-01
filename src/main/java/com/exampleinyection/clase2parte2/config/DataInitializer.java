package com.exampleinyection.clase2parte2.config;

import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import com.exampleinyection.clase2parte2.repository.AllergyRepository;
import com.exampleinyection.clase2parte2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;

    public DataInitializer(UserRepository userRepository, AllergyRepository allergyRepository) {
        this.userRepository = userRepository;
        this.allergyRepository = allergyRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // En M2M las alergias son entidades independientes, se crean una sola vez
        List<Allergy> allergies = allergyRepository.saveAll(List.of(
            new Allergy("Pollen",    1),
            new Allergy("Peanuts",   3),
            new Allergy("Gluten",    2),
            new Allergy("Lactose",   2),
            new Allergy("Shellfish", 4)
        ));

        for (int i = 1; i <= 30; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setAge(20 + (i % 50));
            // Cada usuario comparte un subconjunto de las alergias comunes
            user.setAllergies(allergies.subList(0, (i % 5) + 1));
            userRepository.save(user);
        }
    }
}
