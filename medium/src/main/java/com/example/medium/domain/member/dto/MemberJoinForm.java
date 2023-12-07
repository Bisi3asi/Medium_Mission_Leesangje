package com.example.medium.domain.member.dto;

import com.example.medium.global.dto.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJoinForm {
    @NotBlank(message = "id를 입력해주세요.")
    @Size(max = 20, min = 8, message = "id는 8자 ~ 20자로 입력해주세요.")
    private final String username;

    @NotBlank(message = "id를 입력해주세요.")
    @Size(max = 20, min = 8, message = "id는 8자 ~ 20자로 입력해주세요.")
    private final String password;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 20, min = 8, message = "비밀번호는 8자 ~ 20자로 입력해주세요.")
    private final String passwordConfirm;

    public static ResponseDto<MemberJoinForm> of(String username, String password, String passwordConfirm){
        if (!Objects.equals(password, passwordConfirm)){
            return ResponseDto.of("400", "password confirm failed", new MemberJoinForm(username, password, passwordConfirm));
        }
        return ResponseDto.of("200", "success", new MemberJoinForm(username, password, passwordConfirm));
    }
}
