package com.example.medium.global.security;

import com.example.medium.domain.member.entity.Member;
import com.example.medium.domain.member.service.MemberService;
import com.example.medium.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final MemberService memberService;
    private final Rq rq;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) {
        String accessToken = rq.getAccessTokenFromCookie(null);
        String refreshToken = rq.getRefreshTokenFromCookie(null);

        if (accessToken != null) {
            SecurityUser user = memberService.getUserFromAccessToken(accessToken);
            if (user == null) { // user == null 일 때 : accessToken이 만료난 경우
                Member member = memberService.findByRefreshToken(refreshToken);
                // accessToken 재생성
                String newAccessToken = memberService.makeToken(member, 10);
                // 새로운 accessToken으로 SecurityUser 값 변경
                user = memberService.getUserFromAccessToken(newAccessToken);
                // newAccessToken으로 accessToken 값 변경
                rq.setAccessTokenToCookie(newAccessToken);
                System.out.println("accessToken refresh, update cookie success");
            }
            rq.setAuthentication(user);
        }

        filterChain.doFilter(req, resp);
    }
}
