package com.boardtest.dreamchaser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ★★★ 1. 정적 리소스는 Security 필터를 아예 거치지 않도록 설정 (가장 확실한 방법) ★★★
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // /css/**, /js/**, /images/**, /uploads/** 등 정적 리소스에 대한 요청은 인증/인가 절차를 무시
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        // ★★★ 2. 정적 리소스 경로를 여기서 제거 (webSecurityCustomizer가 처리하므로) ★★★
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/", "/user/**", "/post/list").permitAll()
                        // 상세 페이지 등, 나머지 모든 요청은 인증을 요구
                        .anyRequest().authenticated())

                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/", true))

                .logout((logout) -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))

                .csrf((csrf) -> csrf.disable())
        ;
        return http.build();
    }
}

