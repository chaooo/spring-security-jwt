package com.example.jwt.config.security;

import com.alibaba.fastjson.JSON;
import com.example.jwt.entity.ResponseJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证拦截器
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    public AuthenticationEntryPointImpl() {}

    /**
     * @param request 遇到了认证异常authException用户请求
     * @param response 将要返回给客户的相应
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.debug("预认证入口被调用。拒绝访问，AuthenticationException={}", exception.getMessage());
        // 没有权限，返回403
        // response.sendError(403, "Access Denied");
        ResponseJson<Void> errorJson = ResponseJson.error(-99, "访问失败，需要身份认证", null);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(errorJson));
    }

}
