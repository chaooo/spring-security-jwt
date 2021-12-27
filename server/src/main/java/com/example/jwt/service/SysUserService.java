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
     * 获取角色列表
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    ResponseJson<PaginationVo<SysRole>> getRoleList(int pageNum, int pageSize);
    /**
     * 根据ID获取角色
     */
    ResponseJson<SysRole> getRoleInfo(Integer id);
    /**
     * 根据ID更新角色
     */
    ResponseJson<Void> updateRole(Integer id, SysRole sysRole);
    /**
     * 添加角色
     */
    ResponseJson<Void> addRole(SysRole sysRole);
    /**
     * 根据ID删除角色
     */
    ResponseJson<Void> deleteRole(Integer id);
    /**
     * 角色授权菜单
     */
    ResponseJson<Void> setRoleMenu(Integer id, SysRole sysRole);

    /**
     * 获取菜单列表
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    ResponseJson<PaginationVo<SysMenu>> getMenuList(Integer parentId, Integer status, int pageNum, int pageSize);
    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    ResponseJson<List<SysMenu>> getMenuTree();
    /**
     * 根据ID获取菜单
     */
    ResponseJson<SysMenu> getMenu(Integer id);
    /**
     * 根据ID更新菜单
     */
    ResponseJson<Void> updateMenu(Integer id, SysMenu menu);
    /**
     * 根据ID(可选择的)更新菜单字段
     */
    ResponseJson<Void> updateMenuSelective(Integer id, SysMenu menu);
    /**
     * 添加菜单
     */
    ResponseJson<Void> addMenu(SysMenu menu);
    /**
     * 根据ID删除菜单
     */
    ResponseJson<Void> deleteMenu(Integer id);

}
