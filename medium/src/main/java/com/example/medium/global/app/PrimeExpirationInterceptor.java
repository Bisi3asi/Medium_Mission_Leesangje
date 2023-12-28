package com.example.medium.global.app;

import com.example.medium.domain.member.service.MemberService;
import com.example.medium.global.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class PrimeExpirationInterceptor implements HandlerInterceptor {
    private final Rq rq;
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler){
        if (rq.isPrimeExpired()){
            memberService.deletePrime(rq.getMember());
        }
        return true;
    }
}
