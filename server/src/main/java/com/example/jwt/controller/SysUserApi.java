package com.example.jwt.controller;

import com.example.jwt.config.security.ConstantKey;
import com.example.jwt.config.security.JwtService;
import com.example.jwt.entity.*;
import com.example.jwt.service.SysUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sys/user")
public class SysUserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private JwtService jwtService;

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public ResponseJson<SysUser> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        String username = jwtService.getUsername(token);
        return sysUserService.getInfoByUsername(username);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public ResponseJson<Void> logout(HttpServletRequest request) {
        String token = request.getHeader(ConstantKey.TOKEN_NAME);
        return sysUserService.logout(token);
    }

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param fullName 姓名
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @GetMapping("/list")
    public ResponseJson<PaginationVo<SysUser>> userList(String username, String fullName,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return sysUserService.getUserList(username, fullName, pageNum, pageSize);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseJson<SysUser> getUser(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.getUser(id);
    }

    /**
     * 根据ID更新用户
     */
    @PostMapping("/update/{id}")
    public ResponseJson<Void> updateUser(@PathVariable("id") Integer id, @RequestBody SysUser sysUser) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.updateUser(id, sysUser);
    }

    /**
     * 添加用户
     */
    @PostMapping("/create")
    public ResponseJson<Void> addUser(@RequestBody SysUser sysUser) {
        return sysUserService.addUser(sysUser);
    }

    /**
     * 根据ID删除用户
     */
    @PostMapping("/delete/{id}")
    public ResponseJson<Void> deleteUser(@PathVariable("id") Integer id) {
        if (null == id || id <= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.deleteUser(id);
    }

    /**
     * 用户修改资料
     */
    @PostMapping("/update/profile")
    public ResponseJson<Void> updateUserProfile( @RequestBody UserProfile profile) {
        if (null == profile.getUserId() || profile.getUserId()<= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.updateUserProfile(profile);
    }

    /**
     * 用户修改密码
     */
    @PostMapping("/update/password")
    public ResponseJson<Void> updateUserPassword( @RequestBody UserProfile profile) {
        if (null == profile.getUserId() || profile.getUserId()<= 0) {
            return ResponseJson.error("用户Id不能为空！", null);
        }
        return sysUserService.updateUserPassword(profile);
    }
}
