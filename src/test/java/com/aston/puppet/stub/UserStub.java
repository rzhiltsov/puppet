package com.aston.puppet.stub;

import com.aston.puppet.dto.AddUserRequest;
import com.aston.puppet.entity.User;

import java.time.LocalDate;

public class UserStub {

    public static User createUser(int i) {
        return User
                .builder()
                .firstName("Иван" + i)
                .lastName("Иванов" + i)
                .birthDate(LocalDate.of(1, 1, 1))
                .inn("12345678901" + i)
                .snils("1234567890" + i)
                .passportNumber("123456789" + i)
                .login("user" + i)
                .password("123" + i)
                .build();
    }

    public static AddUserRequest createAddUserRequest() {
        return AddUserRequest
                .builder()
                .firstName("Иван")
                .lastName("Иванов")
                .birthDate(LocalDate.of(1, 1, 1))
                .inn("123456789012")
                .snils("12345678901")
                .passportNumber("1234567890")
                .login("user")
                .password("123")
                .build();
    }
}
