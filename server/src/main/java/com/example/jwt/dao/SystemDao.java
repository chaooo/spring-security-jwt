package com.example.jwt.dao;


import com.example.jwt.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Mapper
public interface SystemDao {
    /**
     * 根据用户名称查找可登录用户
     */
    SysUser findByUsername(String username);
    Integer getUserIdByUsername(String username);
    /**
     * 新增用户角色关系表
     */
    void insertUserRoleRelation(Integer userId, Integer roleId);
    /**
     * 获取用户角色名称列表
     */
    List<String> getRoleNamesByUserId(Integer userId);
    /**
     * 获取用户角色Id列表
     */
    List<String> getRoleIdsByUserId(Integer userId);
    /**
     * 删除用户原有角色关联
     */
    void deleteUserRoleRelation(Integer userId);
    /**
     * 根据角色Ids获取菜单列表
     */
    List<SysMenu> getMenusByRoleIds(@Param("roleIds") List<Integer> roleIds);
    /**
     * 根据角色Id获取按钮权限列表
     */
    List<String> getPermissionNames(@Param("roleIds") List<Integer> roleIds);

    /**
     * 获取用户列表
     */
    List<SysUser> getUserList(String username, String fullName);
    /**
     * 根据ID获取用户
     */
    SysUser getUser(Integer id);
    /**
     * 新增系统用户
     */
    void insertSysUser(SysUser sysUser);
    /**
     * 根据ID更新用户
     */
    void updateSysUser(SysUser sysUser);
    /**
     * 根据ID删除用户
     */
    void deleteSysUser(Integer id);

    /**
     * 根据用户Id获取用户角色列表
     */
    List<SysRole> getRoleListByUserId(Integer userId);
    /**
     * 获取角色列表
     */
    List<SysRole> getRoleList();
    /**
     * 根据ID获取角色
     */
    SysRole getRole(Integer id);
    /**
     * 根据角色名称获取角色
     */
    SysRole getRoleByRoleName(String roleName);
    /**
     * 根据ID更新角色
     */
    void updateRole(SysRole sysRole);
    /**
     * 添加角色
     */
    void insertRole(SysRole sysRole);
    /**
     * 根据ID删除角色
     */
    void deleteRole(Integer id);
    /**
     * 根据角色ID获取菜单Ids
     */
    List<Integer> getMenuIdsByRoleId(Integer roleId);
    /**
     * 根据角色ID获取（权限-菜单）Ids
     */
    List<String> getPermissionMenuIdsByRoleId(Integer roleId);
    /**
     * 保存角色菜单关系
     */
    void insertRoleMenuRelation(Integer roleId, Integer menuId);
    /**
     * 保存角色权限关系
     */
    void insertRolePermissionRelation(Integer roleId, Integer permissionId);
    /**
     * 删除原有角色菜单关联
     */
    void deleteRoleMenuRelation(Integer roleId);
    /**
     * 删除原有角色权限关联
     */
    void deleteRolePermissionRelation(Integer roleId);

    /**
     * 获取菜单列表
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     */
    List<SysMenu> getMenuList(Integer parentId, Integer status);
    /**
     * 获取树状菜单列表
     * @param parentId 父级菜单Id
     */
    List<SysMenuTree> getMenuTree(String parentId);
    /**
     * 获取当前菜单的按钮权限
     * @param menuId 菜单Id
     */
    List<SysMenuTree> getTreePermissions(String menuId);
    /**
     * 根据ID获取菜单
     */
    SysMenu getMenu(Integer id);
    /**
     * 根据路由名称查询菜单
     */
    SysMenu getMenuByName(String name);
    /**
     * 根据ID更新菜单
     */
    void updateMenu(SysMenu menu);
    /**
     * 根据ID删除菜单
     */
    void deleteMenu(Integer id);
    /**
     * 添加菜单
     */
    void insertMenu(SysMenu menu);
    /**
     * 获取菜单的按钮权限列表
     * @param menuId 菜单Id
     */
    List<SysPermission> getPermissionList(Integer menuId);
    /**
     * 根据菜单Id获取权限ids
     */
    List<Integer> getPermissionIds(Integer menuId);
    /**
     * 权限ID更新权限
     */
    void updatePermission(SysPermission permission);
    /**
     * 权限ID删除权限
     */
    void deletePermission(Integer id);
    /**
     * 添加按钮权限
     */
    void insertPermission(SysPermission permission);
}
