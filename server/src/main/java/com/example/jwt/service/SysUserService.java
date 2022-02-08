package com.example.jwt.service;


import com.example.jwt.entity.*;

import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */

public interface SysUserService {
    /**
     * 获取用户信息
     */
    ResponseJson<SysUser> getInfoByUsername(String username);
    /**
     * 退出登录
     */
    ResponseJson<Void> logout(String token);

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param fullName 姓名
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    ResponseJson<PaginationVo<SysUser>> getUserList(String username, String fullName, int pageNum, int pageSize);
    /**
     * 根据ID获取用户
     */
    ResponseJson<SysUser> getUser(Integer id);
    /**
     * 根据ID更新用户
     */
    ResponseJson<Void> updateUser(Integer id, SysUser sysUser);
    /**
     * 添加用户
     */
    ResponseJson<Void> addUser(SysUser sysUser);
    /**
     * 根据ID删除用户
     */
    ResponseJson<Void> deleteUser(Integer id);

    /**
     * 用户修改资料
     */
    ResponseJson<Void> updateUserProfile(UserProfile profile);
    /**
     * 用户修改密码
     */
    ResponseJson<Void> updateUserPassword(UserProfile profile);
}
