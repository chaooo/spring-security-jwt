package com.example.jwt.config.security;

import com.alibaba.fastjson.JSON;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义JWT登录过滤器
 *
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的3个方法
 *     attemptAuthentication：接收并解析用户凭证。
 *     successfulAuthentication：用户成功登录后被调用，我们在这个方法里生成token。
 *     unsuccessfulAuthentication：认证失败后被调用
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    public JwtLoginFilter(AuthenticationManager authenticationManager, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
    }

    /**
     * 尝试身份认证(接收并解析用户凭证)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
        );
    }

    /**
     * 认证成功(用户成功登录后，这个方法会被调用，我们在这个方法里生成token)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) {
        try {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            // 定义存放角色集合的对象
            List<String> roleList = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authorities) {
                roleList.add(grantedAuthority.getAuthority());
            }
            /*
             * 生成token
             */
            Calendar calendar = Calendar.getInstance();
            // 设置签发时间
            calendar.setTime(new Date());
            Date now = calendar.getTime();
            // 设置过期时间: 5分钟
            calendar.add(Calendar.MINUTE, 5);
            Date time = calendar.getTime();
            String token = Jwts.builder()
                    .setSubject(auth.getName() + "-" + roleList)
                    // 签发时间
                    .setIssuedAt(now)
                    // 过期时间
                    .setExpiration(time)
                    // 自定义算法与签名：这里算法采用HS512，常量中定义签名key
                    .signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY)
                    .compact();
            // 将token存入redis,并设置超时时间为token过期时间
            redisService.set(token, token, time);
            /*
             * 返回token
             */
            log.info("用户登录成功，生成token={}", token);
            // 登录成功后，返回token到header里面
            response.addHeader("Authorization", token);
            // 登录成功后，返回token到body里面
            ResponseJson<String> result = ResponseJson.success("登录成功", token);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("IOException:", e);
        }
    }

    /**
     * 认证失败调用
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.warn("登录失败[{}]，AuthenticationException={}", request.getRequestURI(), exception.getMessage());
        // 登录失败，返回错误信息
        ResponseJson<Void> result = ResponseJson.error(exception.getMessage(), null);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
