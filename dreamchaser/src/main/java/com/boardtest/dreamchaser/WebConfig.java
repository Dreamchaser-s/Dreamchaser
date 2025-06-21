package com.boardtest.dreamchaser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${custom.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ★★★ 경로를 file:/// 로 시작하도록 변경하여 안정성 확보 ★★★
        // Windows 사용자의 경우 file:///C:/.../ 와 같이 슬래시 3개를 사용합니다.
        String resourcePath = "file:///" + uploadPath;

        // 디버깅을 위해 실제 적용되는 경로를 콘솔에 출력
        System.out.println("--- DEBUG: Resource Handler Path Mapped ---");
        System.out.println("/uploads/** -> " + resourcePath);
        System.out.println("-------------------------------------------");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);
    }
}