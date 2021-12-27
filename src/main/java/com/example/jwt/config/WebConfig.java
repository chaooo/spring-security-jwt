package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Configuration
public class WebConfig {
    /**
     * BCryptPasswordEncoder 解析器注入到容器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
