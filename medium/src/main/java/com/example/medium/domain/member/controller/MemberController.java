package com.example.medium.domain.member.controller;

import com.example.medium.domain.member.dto.MemberJoinRequestDto;
import com.example.medium.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // Get: /member/join
    @GetMapping("/join")
    public String showJoinForm(MemberJoinRequestDto memberJoinRequestDto) {
        return "domain/member/join_form";
    }

    // Post: /member/join
    @PostMapping("/join")
    public String join(@ModelAttribute("memberJoinRequestDto") @Valid
                       MemberJoinRequestDto memberJoinRequestDto,
                       BindingResult brs) {

        // 1차 : 비밀번호와 비밀번호 확인 일치 검증 (Controller)
        if (!Objects.equals(memberJoinRequestDto.getPassword(), memberJoinRequestDto.getPasswordConfirm())){
            brs.addError(new ObjectError("password", "please check match of the password and password Confirm"));
            return "domain/member/join_form";
        }
        // 2차 : 동일 id 존재 여부 검증(Service)
        memberService.create(memberJoinRequestDto, brs);
        if (brs.hasErrors()){
            return "domain/member/join_form";
        }

        return "redirect:/member/login";
    }

    // Get: /member/login
    @GetMapping("/login")
    public String showLoginForm() {
        return "domain/member/login_form";
    }

    // Post: /member/login (Spring Security)
    @PostMapping("/login")
    public String login() {
        return "redirect:/";
    }

    // Post: /member/logout
    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
