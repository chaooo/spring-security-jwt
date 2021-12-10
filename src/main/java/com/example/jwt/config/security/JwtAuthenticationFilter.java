package com.example.jwt.config.security;

import com.example.jwt.service.RedisService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

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

    private final RedisService redisService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RedisService redisService) {
        super(authenticationManager);
        this.redisService = redisService;
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
        if (StringUtils.hasLength(token)) {
            String cacheToken = String.valueOf(redisService.get(token));
            if (StringUtils.hasLength(token) && !"null".equals(cacheToken)) {
                String user = null;
                try {
                    Claims claims = Jwts.parser()
                            // 设置生成token的签名key
                            .setSigningKey(ConstantKey.SIGNING_KEY)
                            // 解析token
                            .parseClaimsJws(cacheToken).getBody();
                    // 取出用户信息
                    user = claims.getSubject();
                    // 重设Redis超时时间
                    resetRedisExpire(token, claims);
                } catch (ExpiredJwtException e) {
                    log.info("Token过期续签，ExpiredJwtException={}", e.getMessage());
                    Claims claims = e.getClaims();
                    // 取出用户信息
                    user = claims.getSubject();
                    // 刷新Token
                    refreshToken(token, claims);
                } catch (UnsupportedJwtException e) {
                    log.warn("访问[{}]失败，UnsupportedJwtException={}", request.getRequestURI(), e.getMessage());
                } catch (MalformedJwtException e) {
                    log.warn("访问[{}]失败，MalformedJwtException={}", request.getRequestURI(), e.getMessage());
                } catch (SignatureException e) {
                    log.warn("访问[{}]失败，SignatureException={}", request.getRequestURI(), e.getMessage());
                } catch (IllegalArgumentException e) {
                    log.warn("访问[{}]失败，IllegalArgumentException={}", request.getRequestURI(), e.getMessage());
                }
                if (user != null) {
                    // 获取用户权限和角色
                    String[] split = user.split("-")[1].split(",");
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    for (String s : split) {
                        authorities.add(new GrantedAuthorityImpl(s));
                    }
                    // 返回Authentication
                    return new UsernamePasswordAuthenticationToken(user, null, authorities);
                }
            }
        }
        log.warn("访问[{}]失败，需要身份认证", request.getRequestURI());
        return null;
    }

    /**
     * 重设Redis超时时间
     * 当前时间 + (`cacheToken`过期时间 - `cacheToken`签发时间)
     */
    private void resetRedisExpire(String token, Claims claims) {
        // 当前时间
        long current = System.currentTimeMillis();
        // token签发时间
        long issuedAt = claims.getIssuedAt().getTime();
        // token过期时间
        long expiration = claims.getExpiration().getTime();
        // 当前时间 + (`cacheToken`过期时间 - `cacheToken`签发时间)
        long expireAt = current + (expiration - issuedAt);
        // 重设Redis超时时间
        redisService.expire(token, expireAt);
    }

    /**
     * 刷新Token
     * 刷新Token的时机： 当cacheToken已过期 并且Redis在有效期内
     * 重新生成Token并覆盖Redis的v值(这时候k、v值不一样了)，然后设置Redis过期时间为：新Token过期时间
     */
    private void refreshToken(String token, Claims claims) {
        // 当前时间
        long current = System.currentTimeMillis();
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
        // 将refreshToken覆盖Redis的v值,并设置超时时间为refreshToken过期时间
        redisService.set(token, refreshToken, time);
        // 打印日志
        log.info("刷新token执行时间: {}", (System.currentTimeMillis() - current) + " 毫秒");
    }
}
