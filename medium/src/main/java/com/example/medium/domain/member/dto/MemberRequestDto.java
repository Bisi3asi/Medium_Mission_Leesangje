package com.example.medium.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "id를 입력해주세요.")
    @Size(max = 20, min = 8, message = "id는 8자 ~ 20자로 입력해주세요.")
    private final String username;

    @NotBlank(message = "id를 입력해주세요.")
    @Size(max = 20, min = 8, message = "id는 8자 ~ 20자로 입력해주세요.")
    private final String password;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 20, min = 8, message = "비밀번호는 8자 ~ 20자로 입력해주세요.")
    private final String passwordConfirm;
}
