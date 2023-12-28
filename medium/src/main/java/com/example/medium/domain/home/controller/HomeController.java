package com.example.medium.domain.home.controller;

import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class HomeController {
    private final MemberService memberService;
    @GetMapping("/prime")
    public String showPrime() {
        return "domain/home/prime";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/prime/join")
    public String joinPrime(Principal principal,
                            RedirectAttributes attr) {
        if (principal == null) {
            return "redirect:/";
        }
        ResponseData<Member> resp = memberService.setPrime(memberService.findByUsername(principal.getName()));
        attr.addFlashAttribute("msg", resp.getMsg());
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/prime/cancel")
    public String cancelPrime(Principal principal,
                              RedirectAttributes attr) {
        if (principal == null) {
            return "redirect:/";
        }
        ResponseData<Member> resp = memberService.deletePrime(memberService.findByUsername(principal.getName()));
        attr.addFlashAttribute("msg", resp.getMsg());
        return "redirect:/";
    }
}
