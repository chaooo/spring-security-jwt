package com.example.jwt.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义身份认证验证组件
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Slf4j
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthenticationProviderImpl(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 用来验证用户身份 （对传递的Authentication对象的身份验证）
     *
     * @param authentication 传递的Authentication对象
     * @return 包含凭证的经过完全认证的对象
     * @throws AuthenticationException 份验证失败异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            log.info("用户登录成功，username={}", name);
            // 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
            return new UsernamePasswordAuthenticationToken(name, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }

    /**
     * 判断当前的AuthenticationProvider 是否支持对应的Authentication对象
     * @param authentication Authentication对象
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
