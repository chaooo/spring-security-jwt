package com.example.jwt.dao;


import com.example.jwt.entity.SysMenu;
import com.example.jwt.entity.SysRole;
import com.example.jwt.entity.SysUser;
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
public interface SysUserDao {
    /**
     * 根据用户名称查找可登录用户
     */
    SysUser findByUsername(String username);
    /**
     * 新增用户角色关系表
     */
    void insertUserRoleRelation(Integer userId, Integer roleId);
    /**
     * 获取用户角色名称列表
     */
    List<String> getRoleListByUserId(Integer userId);
    /**
     * 获取用户角色Id列表
     */
    List<String> getRoleIdsByUserId(Integer userId);
    /**
     * 获取用户角色Id列表
     */
    List<Integer> getRoleIdsByUsername(String username);
    /**
     * 删除用户原有角色关联
     */
    void deleteUserRoleRelation(Integer userId);
    /**
     * 根据角色Ids获取菜单列表
     */
    List<SysMenu> getMenusByRoleIds(@Param("roleIds") List<Integer> roleIds);

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
    List<String> getMenuIdsByRoleId(Integer roleId);
    /**
     * 保存角色菜单关系
     */
    void insertRoleMenuRelation(Integer roleId, Integer menuId);
    /**
     * 删除原有角色菜单关联
     */
    void deleteRoleMenuRelation(Integer roleId);

    /**
     * 获取菜单列表
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     */
    List<SysMenu> getMenuList(Integer parentId, Integer status);
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

}
