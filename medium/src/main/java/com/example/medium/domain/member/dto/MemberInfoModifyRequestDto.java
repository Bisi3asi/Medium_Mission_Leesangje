package com.example.medium.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoModifyRequestDto {
    @NotBlank(message = "유저 이름을 입력해주세요.")
    @Size(min = 2, max = 20, message = "유저 이름은 8자 ~ 20자로 입력해주세요.")
    private final String nickname;

    @Size(max = 50, message = "프로필 메세지는 50자 이내로 입력해주세요.")
    private final String profileMsg;
}
