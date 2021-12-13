package com.example.jwt.service;

import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysMenu;
import com.example.jwt.entity.SysUser;

import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */

public interface SysUserService {
    /**
     * 注册接口
     */
    ResponseJson<SysUser> register(SysUser sysUser);
    /**
     * 获取用户可访问菜单
     */
    ResponseJson<List<SysMenu>> menuList(String username);
}
