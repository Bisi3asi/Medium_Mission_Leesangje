package com.example.medium.domain.member.controller;

import com.example.medium.domain.member.dto.MemberInfoModifyRequestDto;
import com.example.medium.domain.member.dto.MemberJoinRequestDto;
import com.example.medium.domain.member.dto.MemberLoginRequestDto;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.global.response.ResponseData;
import com.example.medium.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    // Get: /member/join
    @GetMapping("/join")
    public String showJoinForm(MemberJoinRequestDto memberJoinRequestDto,
                               Principal principal
    ) {
        if (principal != null) {
            return "redirect:/";
        }
        return "domain/member/join_form";
    }

    // Post: /member/join
    @PostMapping("/join")
    public String join(@ModelAttribute("memberJoinRequestDto") @Valid
                       MemberJoinRequestDto memberJoinRequestDto,
                       BindingResult brs,
                       RedirectAttributes attr) {

        // 1차 : 비밀번호와 비밀번호 확인 일치 검증 (Controller)
        if (!Objects.equals(memberJoinRequestDto.getPassword(), memberJoinRequestDto.getPasswordConfirm())) {
            brs.addError(new ObjectError("password", "비밀번호와 비밀번호 확인이 일치하지 않습니다."));
            return "domain/member/join_form";
        }
        // 2차 : 동일 id 존재 여부 검증(Service)
        ResponseData responseData = memberService.create(memberJoinRequestDto, brs);
        if (brs.hasErrors()) {
            return "domain/member/join_form";
        }

        attr.addFlashAttribute("msg", responseData.getMsg());
        return "redirect:/member/login";
    }

    // Get: /member/login
    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute("memberLoginRequestDto") MemberLoginRequestDto memberloginRequestDto,
                                Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        return "domain/member/login_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login/oauth2loginsuccess")
    public String redirectOauth2LoginSuccess(RedirectAttributes attr, Principal principal) {
        Member member = memberService.findByUsername(principal.getName());

        attr.addFlashAttribute("msg", String.format("환영합니다, %s님!", member.getNickname()));
        return "redirect:/";
    }

    // Post: /member/login (JWT token)
    @PostMapping("/login")
    public String login(@ModelAttribute("memberLoginRequestDto") @Valid
                        MemberLoginRequestDto memberLoginRequestDto,
                        BindingResult brs,
                        RedirectAttributes attr) {

        // 1차 : 유효 길이 확인 검증 (DTO)
        if (brs.hasErrors()) {
            return "domain/member/login_form";
        }

        // 2차 : username, password 일치 여부 확인 검증 (service)
        ResponseData<Member> responseData = memberService.checkUsernameAndPassword(memberLoginRequestDto, brs);
        if (brs.hasErrors() || responseData.getResultCode().contentEquals("400")) {
            return "domain/member/login_form";
        }
        Member member = responseData.getData();

        // 로그인 시에 accessToken, refreshToken 재생산
        memberService.setTokenWhenLogin(member);

        attr.addFlashAttribute("msg", responseData.getMsg());
        return "redirect:/";
    }

    // Get: /member/logout
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public String logout(RedirectAttributes attr,
                         Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }

        if (rq.getAccessTokenFromCookie(null) != null) {
            rq.removeAccessTokenFromCookie();
        }
        attr.addFlashAttribute("msg", "로그아웃 되었습니다.");
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String showMyPage(@ModelAttribute MemberInfoModifyRequestDto memberInfoModifyRequestDto,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        return "domain/member/mypage";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/mypage/modify")
    public String modifyMemberInfo(@Valid @ModelAttribute MemberInfoModifyRequestDto memberInfoModifyRequestDto,
                                   BindingResult brs,
                                   RedirectAttributes attr,
                                   Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        if (brs.hasErrors()) {
            return "domain/member/mypage";
        }

        ResponseData<Member> resp = memberService.modify(
                memberService.findByUsername(principal.getName()),
                memberInfoModifyRequestDto
        );

        attr.addFlashAttribute("msg", resp.getMsg());
        return "redirect:/member/mypage";
    }

}
