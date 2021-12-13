package com.example.jwt.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 自定义JWT认证过滤器
 *
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 解析token
        String userinfo = jwtService.parseToken(request);
        if (userinfo != null) {
            // 获取用户权限和角色
            String[] split = userinfo.split("-")[1].split(",");
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            for (String s : split) {
                authorities.add(new GrantedAuthorityImpl(s));
            }
            // 返回Authentication
            return new UsernamePasswordAuthenticationToken(userinfo, null, authorities);
        }
        log.warn("访问[{}]失败，需要身份认证", request.getRequestURI());
        return null;
    }
}
