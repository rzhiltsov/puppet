package com.aston.puppet.dto;

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
public class UpdateUserRequest {

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @Length(min = 12, max = 12)
    private String userInn;

    @Length(min = 11, max = 11)
    private String snils;

    @Length(min = 10, max = 10)
    private String passportNumber;

    private String login;

    private String password;

    @Length(min = 20, max = 20)
    private String paymentAccount;

    @Length(min = 9, max = 9)
    private String bik;

    @Length(min = 20, max = 20)
    private String correspondentAccount;

    @Length(min = 10, max = 10)
    private String bankInn;

    @Length(min = 9, max = 9)
    private String kpp;

    @Length(min = 20, max = 20)
    private String kbk;
}
