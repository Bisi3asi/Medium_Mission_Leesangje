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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR : 해당 회원을 찾을 수 없습니다.")
        );
    }

    public Member findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ERROR: 해당 회원을 찾을 수 없습니다.")
        );
    }

    @Transactional
    public ResponseData create(MemberJoinRequestDto memberRequestDto, BindingResult brs) {
        if (memberRepository.findByUsername(memberRequestDto.getUsername()).isPresent()) {
            brs.addError(
                    new ObjectError(
                            "username", "동일한 ID가 존재합니다. 다른 ID로 시도해주세요."));
            return ResponseData.of("400", "join failed");
        }

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .authorities("admin".equals(memberRequestDto.getUsername()) ? "ROLE_ADMIN" : "ROLE_USER")
                .build();

        memberRepository.save(member);
        return ResponseData.of("200", "회원가입이 완료되었습니다.", member);
    }

    public ResponseData checkUsernameAndPassword(MemberLoginRequestDto memberLoginRequestDto, BindingResult brs) {
        Optional<Member> opMember = memberRepository.findByUsername(memberLoginRequestDto.getUsername());
        if (opMember.isEmpty()) {
            brs.addError(
                    new ObjectError(
                            "login error", "로그인에 실패했습니다, ID와 PW를 확인해주세요."));
            return ResponseData.of("400", "login failed");
        }

        Member member = opMember.get();
        if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
            brs.addError(
                    new ObjectError(
                            "login error", "로그인에 실패했습니다, ID와 PW를 확인해주세요."));
            return ResponseData.of("400", "login failed");
        }

        return ResponseData.of("200", String.format("환영합니다, %s님!", member.getUsername()), member);
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
        long id = Long.parseLong((data.get("id").toString()));
        String username = (String) data.get("username");
        List<? extends GrantedAuthority> authorities =
                ((List<String>) data.get("authorities")).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new SecurityUser(id, username, "", authorities);
    }

    @Transactional
    public void setRefreshToken(Member member, String refreshToken) {
        member.setRefreshToken(refreshToken);
    }
}
