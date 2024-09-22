package com.aston.puppet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String paymentAccount;

    private String kbk;
}
