package com.aston.puppet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private String inn;

    private String snils;

    private String passportNumber;

    private String login;

    private String password;
}
