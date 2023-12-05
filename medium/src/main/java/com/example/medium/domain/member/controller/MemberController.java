package com.example.medium.domain.member.controller;

import com.example.medium.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // Get: /member/join
    @GetMapping("/join")
    public String showJoinForm(){
        return "domain/member/join_form";
    }

    // Post: /member/join
    @PostMapping("/join")
    public String join(){
        return "redirect:/";
    }

    // Get: /member/login
    @GetMapping("/login")
    public String showLoginForm(){
        return "domain/member/login_form";
    }

    // Post: /member/login (Spring Security)
    @PostMapping("/login")
    public String login(){
        return "redirect:/";
    }

    // Post: /member/logout
    @PostMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
}
