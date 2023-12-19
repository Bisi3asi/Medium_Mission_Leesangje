package com.example.medium.global.utils;

import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final MemberService memberService;

    @ModelAttribute("currentUser") // currentUser라는 이름으로 로그인 멤버 정보 모델로 전송
    public Member addSignedInMemberToModel(Principal principal){
        if (principal != null){
            return memberService.findByUsername(principal.getName());
        }
        return null; // 로그인하지 않은 경우 null 리턴
    }
}
