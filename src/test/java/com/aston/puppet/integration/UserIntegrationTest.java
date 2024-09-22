package com.aston.puppet.integration;

import com.aston.puppet.config.IntegrationTestConfig;
import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.dto.UpdateUserRequest;
import com.aston.puppet.entity.Requisites;
import com.aston.puppet.entity.User;
import com.aston.puppet.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER_PATH = "/users";

    private static final int[] FIELD_SALT = {0, 1};

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("adding user")
    @SneakyThrows
    void addUserTest() {
        AddUserRequest addUserRequest = createAddUserRequest();

        mockMvc.perform(post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(addUserRequest)))
                .andExpect(status().isCreated());

        User user = User.builder().login(addUserRequest.getLogin()).build();
        assertTrue(userRepository.findOne(Example.of(user)).isPresent());
    }

    @Test
    @DisplayName("adding user with unique value duplication")
    @SneakyThrows
    void addUserWithDuplicationTest() {
        userRepository.save(createUser(FIELD_SALT[0]));
        AddUserRequest addUserRequest = createAddUserRequest();

        mockMvc.perform(post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(addUserRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("getting user")
    @SneakyThrows
    void getUserTest() {
        User user = userRepository.save(createUser(FIELD_SALT[0]));

        mockMvc.perform(get(USER_PATH + "/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().toString())));
    }

    @Test
    @DisplayName("getting user which not exist")
    @SneakyThrows
    void getUserWhichNotExistTest() {
        mockMvc.perform(get(USER_PATH + "/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getting users list")
    @SneakyThrows
    void getUsersListTest() {
        userRepository.save(createUser(FIELD_SALT[0]));
        userRepository.save(createUser(FIELD_SALT[1]));
        int expectedListSize = 2;

        mockMvc.perform(get(USER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedListSize)));
    }

    @Test
    @DisplayName("getting empty users list")
    @SneakyThrows
    void getEmptyUsersListTest() {
        mockMvc.perform(get(USER_PATH)).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("updating user")
    @SneakyThrows
    void updateUserTest() {
        User user = userRepository.save(createUser(FIELD_SALT[0]));

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLogin("user2");
        mockMvc.perform(patch(USER_PATH + "/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updateUserRequest)))
                .andExpect(status().isOk());

        User actualUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(actualUser);
        assertEquals(updateUserRequest.getLogin(), actualUser.getLogin());
    }

    @Test
    @DisplayName("updating user with unique constraint violation")
    @SneakyThrows
    void updateUserWithDuplicationTest() {
        User user = userRepository.save(createUser(FIELD_SALT[0]));
        User user2 = userRepository.save(createUser(FIELD_SALT[1]));

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setLogin(user2.getLogin());
        mockMvc.perform(patch(USER_PATH + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updateUserRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("updating user which not exist")
    @SneakyThrows
    void updateUserWhichNotExistTest() {
        mockMvc.perform(patch(USER_PATH + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UpdateUserRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleting user")
    @SneakyThrows
    void deleteUserTest() {
        User user = userRepository.save(createUser(FIELD_SALT[0]));

        mockMvc.perform(delete(USER_PATH + "/" + user.getId())).andExpect(status().isOk());
        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    @DisplayName("deleting user which not exist")
    @SneakyThrows
    void deleteUserWhichNotExistTest() {
        mockMvc.perform(delete(USER_PATH + "/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    private User createUser(int fieldSalt) {
        return User
                .builder()
                .firstName("Иван" + fieldSalt)
                .lastName("Иванов" + fieldSalt)
                .birthDate(LocalDate.of(fieldSalt, 1, 1))
                .userInn("12345678901" + fieldSalt)
                .snils("1234567890" + fieldSalt)
                .passportNumber("123456789" + fieldSalt)
                .login("user" + fieldSalt)
                .password("123" + fieldSalt)
                .requisites(createRequisites(fieldSalt))
                .build();
    }

    private Requisites createRequisites(int fieldSalt) {
        return Requisites
                .builder()
                .paymentAccount("1234567890123456789" + fieldSalt)
                .bik("12345678" + fieldSalt)
                .correspondentAccount("0987654321098765432" + fieldSalt)
                .bankInn("098765432" + fieldSalt)
                .kpp("98765432" + fieldSalt)
                .kbk("2345678901234567890" + fieldSalt)
                .build();
    }

    private AddUserRequest createAddUserRequest() {
        return AddUserRequest
                .builder()
                .firstName("Иван")
                .lastName("Иванов")
                .birthDate(LocalDate.of(1, 1, 1))
                .userInn("123456789012")
                .snils("12345678901")
                .passportNumber("1234567890")
                .login("user")
                .password("123")
                .paymentAccount("12345678901234567890")
                .bik("123456789")
                .correspondentAccount("09876543210987654321")
                .bankInn("0987654321")
                .kpp("987654321")
                .kbk("23456789012345678901")
                .build();
    }
}
