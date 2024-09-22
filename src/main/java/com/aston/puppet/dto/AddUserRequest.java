package com.aston.puppet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
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
    private String userInn;

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

    @Length(min = 20, max = 20)
    @NotBlank
    private String paymentAccount;

    @Length(min = 9, max = 9)
    @NotBlank
    private String bik;

    @Length(min = 20, max = 20)
    @NotBlank
    private String correspondentAccount;

    @Length(min = 10, max = 10)
    @NotBlank
    private String bankInn;

    @Length(min = 9, max = 9)
    @NotBlank
    private String kpp;

    @Length(min = 20, max = 20)
    @NotBlank
    private String kbk;
}
