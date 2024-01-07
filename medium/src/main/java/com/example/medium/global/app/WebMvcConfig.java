package com.example.medium.global.app;

import com.example.medium.domain.member.service.MemberService;
import com.example.medium.global.rq.Rq;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final String FILESTORE_PATH = System.getProperty("user.dir") +"/images/";

    private final Rq rq;
    private final MemberService memberService;

    public WebMvcConfig(Rq rq, MemberService memberService) {
        this.rq = rq;
        this.memberService = memberService;
    }

    // 로컬 이미지를 localhost:8090/images/ 로 핸들링 해 URL 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + FILESTORE_PATH);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new PrimeExpirationInterceptor(rq, memberService))
                .addPathPatterns("/**");
    }
}
