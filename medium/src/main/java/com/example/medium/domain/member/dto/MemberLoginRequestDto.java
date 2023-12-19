package com.example.medium.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {
    @NotBlank(message = "please input username.")
    private String username;

    @NotBlank(message = "please input password.")
    private String password;
}
