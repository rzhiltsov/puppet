package com.aston.puppet.integration;

import com.aston.puppet.config.IntegrationTestConfig;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.User;
import com.aston.puppet.exception.AlreadyExistException;
import com.aston.puppet.exception.NoDataException;
import com.aston.puppet.exception.NotFoundException;
import com.aston.puppet.repository.UserRepository;
import com.aston.puppet.service.UserService;
import com.aston.puppet.stub.UserStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("adding user")
    void addUserTest() {
        UUID id = userService.addUser(UserStub.createAddUserRequest());

        assertTrue(userRepository.existsById(id));
    }

    @Test
    @DisplayName("adding user with unique constraint violation")
    void addUserWithUniqueConstraintViolationTest() {
        userService.addUser(UserStub.createAddUserRequest());

        assertThrows(AlreadyExistException.class, () -> userService.addUser(UserStub.createAddUserRequest()));
    }

    @Test
    @DisplayName("getting user")
    void getUserTest() {
        User user = userRepository.save(UserStub.createUser(0));

        assertEquals(user.getLogin(), userService.getUser(user.getId()).getLogin());
    }

    @Test
    @DisplayName("getting user which not exist")
    void getUserWhichNotExistTest() {
        assertThrows(NotFoundException.class, () -> userService.getUser(UUID.randomUUID()));
    }

    @Test
    @DisplayName("getting users list")
    void getUsersListTest() {
        userRepository.save(UserStub.createUser(0));
        userRepository.save(UserStub.createUser(1));

        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    @DisplayName("getting empty users list")
    void getEmptyUsersListTest() {
        assertThrows(NoDataException.class, () -> userService.getAllUsers());
    }

    @Test
    @DisplayName("updating user")
    void updateUserTest() {
        User user = userRepository.save(UserStub.createUser(0));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLogin("user2");

        userService.updateUser(user.getId(), updateUserRequest);
        assertEquals(updateUserRequest.getLogin(), userService.getUser(user.getId()).getLogin());
    }

    @Test
    @DisplayName("updating user with unique constraint violation")
    void updateUserWithUniqueConstraintViolationTest() {
        User user = userRepository.save(UserStub.createUser(0));
        User user2 = userRepository.save(UserStub.createUser(1));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setInn(user2.getInn());

        assertThrows(AlreadyExistException.class, () -> userService.updateUser(user.getId(), updateUserRequest));
    }

    @Test
    @DisplayName("updating user which not exist")
    void updateUserWhichNotExistTest() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        assertThrows(NotFoundException.class, () -> userService.updateUser(UUID.randomUUID(), updateUserRequest));
    }

    @Test
    @DisplayName("deleting user")
    void deleteUserTest() {
        User user = userRepository.save(UserStub.createUser(0));

        userService.deleteUser(user.getId());
        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    @DisplayName("deleting user which not exist")
    void deleteUserWhichNotExistTest() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(UUID.randomUUID()));
    }
}
