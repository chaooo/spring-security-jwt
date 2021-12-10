package com.example.jwt.service;

import com.example.jwt.dao.SysUserDao;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Service
public class SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseJson<SysUser> register(SysUser sysUser) {
        if (StringUtils.hasLength(sysUser.getUsername()) && StringUtils.hasLength(sysUser.getPassword())) {
            String encodePassword = passwordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(encodePassword);
            sysUserDao.insertSysUser(sysUser);
            return ResponseJson.success("注册成功", sysUser);
        }
        return ResponseJson.error("用户名或密码不能为空", null);
    }
}
