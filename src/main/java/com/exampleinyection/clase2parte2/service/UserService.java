package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1;
    private final AppConfig appConfig;

    public UserService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public User saveUser(User user) {

        String nombreFinal = (user.getNombre() == null || user.getNombre().isBlank())
                ? appConfig.getDefaults().getName()
                : user.getNombre();

        int edadFinal = (user.getEdad() <= 0)
                ? appConfig.getDefaults().getAge()
                : user.getEdad();

        User newUser = new User(
                nextId++,
                nombreFinal,
                edadFinal,
                user.getAllergy()
        );
        users.add(newUser);
        return newUser;
    }

    public User getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("No existe el usuario con id: " + id));
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        users.remove(user);
    }

    public User updateUser(Long id, User userDetails) {

        User existingUser = getUserById(id);

        if (appConfig.getUpdate().isDisabled()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    appConfig.getUpdate().getMessage()
            );
        }

        existingUser.setNombre(userDetails.getNombre());
        existingUser.setEdad(userDetails.getEdad());
        existingUser.setAllergy(userDetails.getAllergy());

        return existingUser;
    }

    public List<User> searchByName(String nombre) {
        return users.stream()
                .filter(user -> StringUtils.containsIgnoreCase(user.getNombre(), nombre))
                .toList();
    }

    public List<User> saveMultipleUsers(List<User> newUsers) {
        return newUsers.stream()
                .map(this::saveUser)
                .toList();
    }

    public List<User> getPaginatedUsers(int page, int size) {
        if (page < 1) page = 1;

        int maxSize = appConfig.getPagination().getMaxSize();
        int finalSize = Math.min(size, maxSize);

        int skip = (page - 1) * finalSize;

        return users.stream()
                .skip(skip)
                .limit(finalSize)
                .toList();
    }

    public void deleteAllUsers() {
        users.clear();
    }
}