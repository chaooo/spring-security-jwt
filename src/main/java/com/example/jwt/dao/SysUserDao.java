package com.example.jwt.dao;

import com.example.jwt.entity.SysMenu;
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
     * 根据用户名称查找用户
     */
    SysUser findByUsername(String username);

    /**
     * 新增系统用户
     */
    void insertSysUser(SysUser sysUser);

    /**
     * 新增用户角色关系表
     */
    void insertUserRoleRelation(Long userId, Long roleId);

    /**
     * 获取用户角色名称列表
     */
    List<String> getRoleList(Long userId);

    /**
     * 获取用户角色Id列表
     */
    List<Long> getRoleIdsByUserId(String username);

    /**
     * 根据角色Ids获取菜单列表
     */
    List<SysMenu> getMenuListByRoleIds(@Param("roleIds") List<Long> roleIds);
}
