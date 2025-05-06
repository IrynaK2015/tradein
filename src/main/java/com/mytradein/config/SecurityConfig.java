package com.mytradein.config;

import com.mytradein.service.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .requestMatchers(Constants.PATH_STYLES, Constants.PATH_API, Constants.PATH_REMINDER, Constants.PATH_INIT).permitAll()
                    .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                    .accessDeniedPage(Constants.PATH_UNAUTHORIZED)
                .and()
                .formLogin()
                    .loginPage(Constants.PATH_LOGIN)
                    .loginProcessingUrl(Constants.PATH_DOLOGIN)
                    .failureUrl(Constants.PATH_FAILED_LOGIN)
                    .usernameParameter(Constants.PARAM_LOGIN)
                    .passwordParameter(Constants.PARAM_PASSWORD)
                    .defaultSuccessUrl(Constants.PATH_DEFAULT_SUCCESS, true)
                        .permitAll()
                .and()
                    .logout()
                    .permitAll()
                    .logoutUrl(Constants.PATH_LOGOUT)
                    .logoutSuccessUrl(Constants.PATH_LOGIN);

        return http.build();
    }
}