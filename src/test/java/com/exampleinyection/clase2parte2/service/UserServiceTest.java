package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.exception.UserNotFoundException;
import com.exampleinyection.clase2parte2.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppConfig appConfig;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_withNullName() {
        when(appConfig.getCommon().getDefaults().getName()).thenReturn("DefaultName");
        when(appConfig.getCommon().getDefaults().getAge()).thenReturn(18);

        UserRequest request = new UserRequest(null, -1, null);
        User user = userService.saveUser(request);
        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void saveUser_withNullAgeAndEmptyName() {
        when(appConfig.getCommon().getDefaults().getName()).thenReturn("DefaultName");
        when(appConfig.getCommon().getDefaults().getAge()).thenReturn(18);

        UserRequest request = new UserRequest("", null, null);
        User user = userService.saveUser(request);
        assertEquals("DefaultName", user.getName());
        assertEquals(18, user.getAge());
    }

    @Test
    void saveUser_withValidData() {
        UserRequest request = new UserRequest("Pepe", 20, null);
        User user = userService.saveUser(request);
        assertEquals("Pepe", user.getName());
        assertEquals(20, user.getAge());
    }

    @Test
    void getUserById_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void deleteUser() {
        User u = userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.deleteUser(u.getId());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(u.getId()));
    }

    @Test
    void updateUser() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));
        User updatedUser = userService.updateUser(user.getId(), new UserRequest("Paco", 22, null));
        assertEquals("Paco", updatedUser.getName());
        assertEquals(22, updatedUser.getAge());
    }

    @Test
    void updateUser_partialAndEmpty() {
        User user = userService.saveUser(new UserRequest("Pepe", 20, null));

        User updatedUser = userService.updateUser(user.getId(), new UserRequest("   ", 0, java.util.List.of(new com.exampleinyection.clase2parte2.model.Allergy("Peanuts", 3))));
        assertEquals("Pepe", updatedUser.getName());
        assertEquals(20, updatedUser.getAge());
        assertNotNull(updatedUser.getAllergies());

        User updatedUserWithNulls = userService.updateUser(user.getId(), new UserRequest(null, null, null));
        assertEquals("Pepe", updatedUserWithNulls.getName());
        assertEquals(20, updatedUserWithNulls.getAge());
    }

    @Test
    void updateUser_disabled() {
        when(appConfig.getUpdate().isDisabled()).thenReturn(true);
        when(appConfig.getUpdate().getMessage()).thenReturn("Disabled");
        User u = userService.saveUser(new UserRequest("Pepe", 20, null));
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(u.getId(), new UserRequest("Paco", 22, null)));
    }

    @Test
    void searchByName() {
        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Pedro", 20, null));
        assertEquals(2, userService.searchByName("pe").size());
    }

    @Test
    void saveMultipleUsers() {
        List<User> users = userService.saveMultipleUsers(List.of(
                new UserRequest("Pepe", 20, null),
                new UserRequest("Paco", 20, null)
        ));
        assertEquals(2, users.size());
    }

    @Test
    void getPaginatedUsers() {
        when(appConfig.getCommon().getPagination().getMaxSize()).thenReturn(100);
        
        userService.saveUser(new UserRequest("Pepe", 20, null));
        userService.saveUser(new UserRequest("Paco", 20, null));
        assertEquals(2, userService.getPaginatedUsers(-1, 10).size());
        assertEquals(2, userService.getPaginatedUsers(1, 10).size());
        assertEquals(1, userService.getPaginatedUsers(2, 1).size());
        assertEquals(appConfig, userService.getAppConfig());
        userService.deleteAllUsers();
        assertEquals(0, userService.getUsers().size());
        userService.getNextId();
    }
}
