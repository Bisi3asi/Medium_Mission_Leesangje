package com.example.medium.domain.member.service;

import com.example.medium.domain.member.dto.MemberJoinRequestDto;
import com.example.medium.domain.member.dto.MemberLoginRequestDto;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.repository.MemberRepository;
import com.example.medium.global.response.ResponseData;
import com.example.medium.global.security.SecurityUser;
import com.example.medium.global.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found")
        );
    }

    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
    }

    @Transactional
    public ResponseData create(MemberJoinRequestDto memberRequestDto, BindingResult brs) {
        if (memberRepository.findByUsername(memberRequestDto.getUsername()).isPresent()) {
            brs.addError(
                    new ObjectError(
                            "username", "username already exists, please try another one."));
            return ResponseData.of("400", "join failed");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authorities("admin".equals(memberRequestDto.getUsername()) ? "ROLE_ADMIN" : "ROLE_USER")
                .build();

        memberRepository.save(member);
        return ResponseData.of("200", "you have successfully joined", member);
    }

    public ResponseData checkUsernameAndPassword(MemberLoginRequestDto memberLoginRequestDto, BindingResult brs) {
        Optional<Member> opMember = memberRepository.findByUsername(memberLoginRequestDto.getUsername());
        if (opMember.isEmpty()) {
            brs.addError(
                    new ObjectError(
                            "login error", "login failed, please check username and password."));
            return ResponseData.of("400", "login failed");
        }

        Member member = opMember.get();
        if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
            brs.addError(
                    new ObjectError(
                            "login error", "login failed, please check username and password."));
            return ResponseData.of("400", "login failed");
        }

        return ResponseData.of("200", String.format("welcome, %s!", member.getUsername()), member);
    }

    public String makeToken(Member member, int minute) {
        return JwtUtil.encodeToken(
                Map.of(
                        "id", member.getId().toString(),
                        "username", member.getUsername(),
                        "authorities", member.getAuthoritiesAsStrList()
                ), minute);
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Claims claims = JwtUtil.decodeToken(accessToken);
        if (claims == null) {
            return null;
        }

        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        long id = Long.parseLong((String) data.get("id"));
        String username = (String) data.get("username");
        List<? extends GrantedAuthority> authorities =
                ((List<String>) data.get("authorities")).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new SecurityUser(id, username, "", authorities);
    }

    public ResponseDto<Member> checkUsernameAndPassword(String username, String password) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "login failed: check Id or password")
        );

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "login failed: check Id or password");
        }

        return ResponseDto.of("200", String.format("welcome, %s!", username), member);
    }
}
