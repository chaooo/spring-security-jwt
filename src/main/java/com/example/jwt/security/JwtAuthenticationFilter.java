package com.example.jwt.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        /*
         * 解析token
         */
        String token = request.getHeader("Authorization");
        if (!ObjectUtils.isEmpty(token)) {
            try {
                Claims claims = Jwts.parser()
                        // 设置生成token的签名key
                        .setSigningKey(ConstantKey.SIGNING_KEY)
                        // 解析token
                        .parseClaimsJws(token).getBody();
                String user = claims.getSubject();
                if (user != null) {
                    String[] split = user.split("-")[1].split(",");
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    for (String s : split) {
                        authorities.add(new GrantedAuthorityImpl(s));
                    }
                    // 刷新Token
                    refreshToken(response, claims);
                    // 返回Authentication
                    return new UsernamePasswordAuthenticationToken(user, null, authorities);
                }
            } catch (ExpiredJwtException e) {
                log.warn("访问[{}]失败，ExpiredJwtException={}", request.getRequestURI(), e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("访问[{}]失败，UnsupportedJwtException={}", request.getRequestURI(), e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("访问[{}]失败，MalformedJwtException={}", request.getRequestURI(), e.getMessage());
            } catch (SignatureException e) {
                log.warn("访问[{}]失败，SignatureException={}", request.getRequestURI(), e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("访问[{}]失败，IllegalArgumentException={}", request.getRequestURI(), e.getMessage());
            }
        }
        log.warn("访问[{}]失败，需要身份认证", request.getRequestURI());
        return null;
    }

    /**
     * 刷新Token
     * 刷新Token的时机：
     * 1. 当前时间 < token过期时间
     * 2. 当前时间 > (签发时间 + (token过期时间 - token签发时间)/2)
     */
    private void refreshToken(HttpServletResponse response, Claims claims) {
        // 当前时间
        long current = System.currentTimeMillis();
        // token签发时间
        long issuedAt = claims.getIssuedAt().getTime();
        // token过期时间
        long expiration = claims.getExpiration().getTime();
        // (当前时间 < token过期时间) && (当前时间 > (签发时间 + (token过期时间 - token签发时间)/2))
        if ((current < expiration) && (current > (issuedAt + ((expiration - issuedAt) / 2)))) {
            /*
             * 重新生成token
             */
            Calendar calendar = Calendar.getInstance();
            // 设置签发时间
            calendar.setTime(new Date());
            Date now = calendar.getTime();
            // 设置过期时间: 5分钟
            calendar.add(Calendar.MINUTE, 5);
            Date time = calendar.getTime();
            String refreshToken = Jwts.builder()
                    .setSubject(claims.getSubject())
                    // 签发时间
                    .setIssuedAt(now)
                    // 过期时间
                    .setExpiration(time)
                    // 算法与签名(同生成token)：这里算法采用HS512，常量中定义签名key
                    .signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY)
                    .compact();
            // 主动刷新token，并返回给前端
            response.addHeader("refreshToken", refreshToken);
            log.info("刷新token执行时间: {}", (System.currentTimeMillis() - current) + " 毫秒");
        }
    }
}
