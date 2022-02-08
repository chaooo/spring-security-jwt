package com.example.jwt.service;

import com.example.jwt.entity.PaginationVo;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysRole;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/2/8
 */
public interface SysRoleService {
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
}
