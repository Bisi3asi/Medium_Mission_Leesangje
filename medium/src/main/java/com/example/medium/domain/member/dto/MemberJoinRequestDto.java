package com.example.medium.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequestDto {
    @NotBlank(message = "please input username.")
    @Size(min = 8, max = 20, message = "username length should be 8 ~ 20.")
    private final String username;

    @NotBlank(message = "please input password.")
    @Size(min = 8, max = 20, message = "password length should be 8 ~ 20.")
    private final String password;

    @NotBlank(message = "please input password confirm.")
    @Size(min = 8, max = 20, message = "password length should be 8 ~ 20.")
    private final String passwordConfirm;
}
