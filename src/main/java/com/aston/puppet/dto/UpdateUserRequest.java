package com.aston.puppet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @Length(min = 12, max = 12)
    private String inn;

    @Length(min = 11, max = 11)
    private String snils;

    @Length(min = 10, max = 10)
    private String passportNumber;

    private String login;

    private String password;
}
