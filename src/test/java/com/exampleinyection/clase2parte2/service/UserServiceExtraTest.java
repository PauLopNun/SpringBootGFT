package com.exampleinyection.clase2parte2.service;

import com.exampleinyection.clase2parte2.config.AppConfig;
import com.exampleinyection.clase2parte2.dto.UserRequest;
import com.exampleinyection.clase2parte2.model.Allergy;
import com.exampleinyection.clase2parte2.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceExtraTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private AppConfig appConfig;

    @InjectMocks
    private UserService userService;

    @Test
    void testBlankNameUsesDefault() {
        when(appConfig.getCommon().getDefaults().getName()).thenReturn("DefaultName");

        UserRequest request = new UserRequest("   ", 25, List.of(new Allergy("Pollen", 2)));
        User savedUser = userService.saveUser(request);

        assertEquals("DefaultName", savedUser.getName());
    }

    @Test
    void testNegativeAgeUsesDefault() {
        when(appConfig.getCommon().getDefaults().getAge()).thenReturn(18);

        UserRequest request = new UserRequest("John", -10, null);
        User savedUser = userService.saveUser(request);

        assertEquals(18, savedUser.getAge());
    }

    @Test
    void testZeroSizePaginationReturnsEmptyList() {
        when(appConfig.getCommon().getPagination().getMaxSize()).thenReturn(100);

        userService.saveUser(new UserRequest("A", 20, null));

        List<User> paginatedUsers = userService.getPaginatedUsers(1, 0);
        assertEquals(0, paginatedUsers.size());
    }

    @Test
    void testExceedMaxSizePaginationReturnsMaxAllowed() {
        when(appConfig.getCommon().getPagination().getMaxSize()).thenReturn(1);

        userService.saveUser(new UserRequest("A", 20, null));
        userService.saveUser(new UserRequest("B", 20, null));

        List<User> paginatedUsers = userService.getPaginatedUsers(1, 10);
        assertEquals(1, paginatedUsers.size());
    }
}
