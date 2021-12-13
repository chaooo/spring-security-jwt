package com.example.jwt.config.security;

import com.example.jwt.service.RedisService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * JWT 服务工具类
 *
 * @author : Charles
 * @date : 2021/12/13
 */
@Slf4j
@Service
public class JwtService {
    @Resource
    private RedisService redisService;
    /**
     * 生成token
     * @param username 用户名
     * @param roleList 角色列表
     */
    public String createToken(String username, List<String> roleList) {
        Calendar calendar = Calendar.getInstance();
        // 设置签发时间
        calendar.setTime(new Date());
        Date now = calendar.getTime();
        // 设置过期时间
        calendar.add(Calendar.MINUTE, ConstantKey.TOKEN_EXPIRE);
        Date time = calendar.getTime();
        String token = Jwts.builder()
                .setSubject(username + "-" + roleList)
                // 签发时间
                .setIssuedAt(now)
                // 过期时间
                .setExpiration(time)
                // 自定义算法与签名：这里算法采用HS512，常量中定义签名key
                .signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY)
                .compact();
        // 将token存入redis,并设置超时时间为token过期时间
        long expire = time.getTime() - now.getTime();
        redisService.set(token, token, expire);
        return token;
    }

    /**
     * 解析Token
     */
    public String parseToken(HttpServletRequest request) {
        String userinfo = null;
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        if (StringUtils.hasLength(token)) {
            String cacheToken = String.valueOf(redisService.get(token));
            if (StringUtils.hasLength(cacheToken) && !"null".equals(cacheToken)) {
                try {
                    Claims claims = Jwts.parser()
                            // 设置生成token的签名key
                            .setSigningKey(ConstantKey.SIGNING_KEY)
                            // 解析token
                            .parseClaimsJws(cacheToken).getBody();
                    // 取出用户信息
                    userinfo = claims.getSubject();
                    // 重设Redis超时时间
                    resetRedisExpire(token, claims);
                } catch (ExpiredJwtException e) {
                    log.info("Token过期续签，ExpiredJwtException={}", e.getMessage());
                    Claims claims = e.getClaims();
                    // 取出用户信息
                    userinfo = claims.getSubject();
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
            }
        }
        return userinfo;
    }

    /**
     * 解析Token,取出用户名（Token过期仍取出用户名）
     */
    public String getUsername(HttpServletRequest request){
        String username = null;
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        if (StringUtils.hasLength(token)) {
            String userinfo = null;
            try {
                Claims claims = Jwts.parser()
                        // 设置生成token的签名key
                        .setSigningKey(ConstantKey.SIGNING_KEY)
                        // 解析token
                        .parseClaimsJws(token).getBody();
                // 取出用户信息
                userinfo = claims.getSubject();
            } catch (ExpiredJwtException e) {
                Claims claims = e.getClaims();
                // 取出用户信息
                userinfo = claims.getSubject();
            } catch (Exception ignored){}
            if (StringUtils.hasLength(userinfo)){
                username = userinfo.split("-")[0];
            }
        }
        return username;
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
        // 设置过期时间: TOKEN_EXPIRE分钟
        calendar.add(Calendar.MINUTE, ConstantKey.TOKEN_EXPIRE);
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
        long expire = time.getTime() - now.getTime();
        redisService.set(token, token, expire);
        // 打印日志
        log.info("刷新token执行时间: {}", (System.currentTimeMillis() - current) + " 毫秒");
    }
}
