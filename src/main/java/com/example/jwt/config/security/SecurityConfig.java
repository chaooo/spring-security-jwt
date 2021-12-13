package com.example.jwt.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * SpringSecurity配置类
 * 通过继承 WebSecurityConfigurerAdapter 实现自定义Security策略
 * `@Configuration`：声明当前类是一个配置类
 * `@EnableWebSecurity`：开启WebSecurity模式
 * `@EnableGlobalMethodSecurity(securedEnabled=true)`：开启注解，支持方法级别的权限控制
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtService jwtService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 全局请求忽略规则配置
     */
    @Override
    public void configure(WebSecurity web) {
        // 需要放行的URL
        web.ignoring().antMatchers("/register", "/hello");
    }

    /**
     * 自定义认证策略：登录的时候会进入
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // 2. 通过实现 AuthenticationProvider 自定义身份认证验证组件
        auth.authenticationProvider(new AuthenticationProviderImpl(userDetailsService, bCryptPasswordEncoder));
    }

    /**
     * 自定义 HTTP 验证规则
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 关闭Session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // 所有请求需要身份认证
            .and().authorizeRequests().anyRequest().authenticated()
            .and()
            // 自定义JWT登录过滤器
            .addFilter(new JwtLoginFilter(authenticationManager(), jwtService))
            // 自定义JWT认证过滤器
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
            // 自定义认证拦截器，也可以直接使用内置实现类Http403ForbiddenEntryPoint
            .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPointImpl())
            // 允许跨域
            .and().cors()
            // 禁用跨站伪造
            .and().csrf().disable();
    }
}
