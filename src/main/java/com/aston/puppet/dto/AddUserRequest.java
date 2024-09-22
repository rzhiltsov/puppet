package com.aston.puppet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AddUserRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate birthDate;

    @Length(min = 12, max = 12)
    @NotBlank
    private String inn;

    @Length(min = 11, max = 11)
    @NotBlank
    private String snils;

    @Length(min = 10, max = 10)
    @NotBlank
    private String passportNumber;

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
