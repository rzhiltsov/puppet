package com.aston.puppet.module;

import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.User;
import com.aston.puppet.exception.AlreadyExistException;
import com.aston.puppet.exception.NoDataException;
import com.aston.puppet.exception.NotFoundException;
import com.aston.puppet.mapper.UserMapper;
import com.aston.puppet.repository.UserRepository;
import com.aston.puppet.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceModuleTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("adding user")
    void addUserTest() {
        when(userMapper.toUser(any(AddUserRequest.class))).thenReturn(new User());

        userService.addUser(new AddUserRequest());
        verify(userRepository, only()).save(any(User.class));
    }

    @Test
    @DisplayName("adding user with unique constraint violation")
    void addUserWithUniqueConstraintViolationTest() {
        when(userMapper.toUser(any(AddUserRequest.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(AlreadyExistException.class, () -> userService.addUser(new AddUserRequest()));
        verify(userRepository, only()).save(any(User.class));
    }

    @Test
    @DisplayName("getting user")
    void getUserTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));

        userService.getUser(UUID.randomUUID());
        verify(userRepository, only()).findById(any(UUID.class));
    }

    @Test
    @DisplayName("getting user which not exist")
    void getUserWhichNotExistTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(UUID.randomUUID()));
        verify(userRepository, only()).findById(any(UUID.class));
    }

    @Test
    @DisplayName("getting users list")
    void getUsersListTest() {
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        userService.getAllUsers();
        verify(userRepository, only()).findAll();
    }

    @Test
    @DisplayName("getting empty users list")
    void getEmptyUsersListTest() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(NoDataException.class, () -> userService.getAllUsers());
        verify(userRepository, only()).findAll();
    }

    @Test
    @DisplayName("updating user")
    void updateUserTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));

        userService.updateUser(UUID.randomUUID(), new UpdateUserRequest());
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("updating user with unique constraint violation")
    void updateUserWithUniqueConstraintViolationTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException(""));

        assertThrows(AlreadyExistException.class,
                () -> userService.updateUser(UUID.randomUUID(), new UpdateUserRequest()));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("updating user which not exist")
    void updateUserWhichNotExistTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(UUID.randomUUID(), new UpdateUserRequest()));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("deleting user")
    void deleteUserTest() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(true);

        userService.deleteUser(UUID.randomUUID());
        verify(userRepository, times(1)).existsById(any(UUID.class));
        verify(userRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("deleting user which not exist")
    void deleteUserWhichNotExistTest() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(UUID.randomUUID()));
        verify(userRepository, times(1)).existsById(any(UUID.class));
        verify(userRepository, never()).deleteById(any(UUID.class));
    }
}
