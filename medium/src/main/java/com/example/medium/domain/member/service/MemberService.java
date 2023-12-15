package com.example.medium.domain.member.service;

import com.example.medium.domain.member.dto.MemberRequestDto;
import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.repository.MemberRepository;
import com.example.medium.global.dto.ResponseDto;
import com.example.medium.global.security.SecurityUser;
import com.example.medium.global.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public ResponseDto<Member> create(MemberRequestDto memberRequestDto) {
        if (!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("please check out password, password confirm.");
        }
        if (memberRepository.findByUsername(memberRequestDto.getUsername()).isPresent()) {
            throw new DuplicateKeyException("username already exists, please choose another one.");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authorities("admin".equals(memberRequestDto.getUsername()) ? "ROLE_ADMIN" : "ROLE_USER")
                .build();

        memberRepository.save(member);
        return ResponseDto.of("200", "you have successfully joined", member);
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Claims claims = JwtUtil.decode(accessToken);

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
