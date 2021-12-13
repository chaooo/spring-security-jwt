package com.example.jwt.controller;

import com.example.jwt.config.security.JwtService;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysMenu;
import com.example.jwt.entity.SysUser;
import com.example.jwt.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@RestController
public class SysUserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private JwtService jwtService;
    /**
     * 注册接口
     */
    @PostMapping("/register")
    public ResponseJson<SysUser> register(SysUser sysUser) {
        return sysUserService.register(sysUser);
    }

    /**
     * 获取用户可访问菜单
     */
    @GetMapping("/menu")
    public ResponseJson<List<SysMenu>> menuList(HttpServletRequest request) {
        String username = jwtService.getUsername(request);
        return sysUserService.menuList(username);
    }

    /**
     * 测试公开接口
     */
    @GetMapping("/hello")
    public ResponseJson<Void> hello() {
        return ResponseJson.success("访问成功！公开接口：/hello",null);
    }

    /**
     * 测试需要认证的接口
     */
    @GetMapping("/private")
    public ResponseJson<Void> hello2() {
        return ResponseJson.success("访问成功！非公开接口：/private", null);
    }
}
