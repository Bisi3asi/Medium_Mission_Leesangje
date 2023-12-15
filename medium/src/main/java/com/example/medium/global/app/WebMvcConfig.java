package com.example.medium.global.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final String FILESTORE_PATH = System.getProperty("user.dir") +"/images/";

    // 로컬 이미지를 localhost:8090/images/ 로 핸들링 해 URL 매핑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + FILESTORE_PATH);
    }
}
