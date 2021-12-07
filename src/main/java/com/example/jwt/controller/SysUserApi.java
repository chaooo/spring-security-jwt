package com.example.jwt.controller;

import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysUser;
import com.example.jwt.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @PostMapping("/register")
    public ResponseJson<SysUser> register(SysUser sysUser) {
        return sysUserService.register(sysUser);
    }

    @GetMapping("/hello")
    public ResponseJson<Void> hello() {
        return ResponseJson.success("访问成功！公开接口：/hello",null);
    }

    @GetMapping("/private")
    public ResponseJson<Void> hello2() {
        return ResponseJson.success("访问成功！非公开接口：/private", null);
    }
}
