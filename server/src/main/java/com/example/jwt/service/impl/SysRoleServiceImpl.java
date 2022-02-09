package com.example.jwt.service.impl;

import com.example.jwt.dao.SystemDao;
import com.example.jwt.entity.PaginationVo;
import com.example.jwt.entity.ResponseJson;
import com.example.jwt.entity.SysRole;
import com.example.jwt.service.SysRoleService;
import com.example.jwt.util.CommonUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/1/6
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SystemDao systemDao;

    /**
     * 获取角色列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseJson<PaginationVo<SysRole>> getRoleList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> roles = systemDao.getRoleList();
        PaginationVo<SysRole> pageData = new PaginationVo<>(roles);
        return ResponseJson.success(pageData);
    }

    /**
     * 根据ID获取角色
     */
    @Override
    public ResponseJson<SysRole> getRoleInfo(Integer id) {
        SysRole sysRole = getRole(id);
        if (null != sysRole) {
            // 根据角色ID获取菜单Ids
            List<Integer> menuIdList = systemDao.getMenuIdsByRoleId(id);
            String menuIds = CommonUtil.idListToString(menuIdList, ",");
            // 根据角色ID获取（权限-菜单）Ids
            List<String> permissionIdList = systemDao.getPermissionMenuIdsByRoleId(id);
            StringBuilder ids = new StringBuilder(menuIds);
            for (String permissionId : permissionIdList) {
                ids.append(",").append(permissionId);
            }
            sysRole.setMenuIds(ids.toString());
        }
        return ResponseJson.success(sysRole);
    }

    private SysRole getRole(Integer id) {
        return systemDao.getRole(id);
    }

    /**
     * 根据ID更新角色
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> updateRole(Integer id, SysRole sysRole) {
        sysRole.setId(id);
        sysRole.setDelFlag(0);
        // 删除原有角色菜单关联
        systemDao.deleteRoleMenuRelation(id);
        // 菜单Ids，用","隔开
        String menuIds = sysRole.getMenuIds();
        systemDao.updateRole(sysRole);
        // 保存角色菜单关系
        saveRoleMenuRelation(sysRole.getId(), menuIds);
        return ResponseJson.success();
    }

    /**
     * 添加角色
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> addRole(SysRole sysRole) {
        if (StringUtils.hasLength(sysRole.getRoleName())) {
            // 查询是否存在当前角色
            SysRole dbRole  = systemDao.getRoleByRoleName(sysRole.getRoleName());
            if (null == dbRole) {
                String menuIds = sysRole.getMenuIds();
                // 保存角色
                systemDao.insertRole(sysRole);
                // 保存角色菜单关系
                saveRoleMenuRelation(sysRole.getId(), menuIds);
                return ResponseJson.success();
            } else if (!Integer.valueOf(0).equals(dbRole.getDelFlag())){
                // 更新已删除角色
                return updateRole(dbRole.getId(), sysRole);
            } else {
                return ResponseJson.error("角色名已存在", null);
            }
        }
        return ResponseJson.error("角色名称不能为空", null);
    }

    /**
     * 根据ID删除角色
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> deleteRole(Integer id) {
        // 删除角色菜单关联
        systemDao.deleteRoleMenuRelation(id);
        // 删除原有角色权限关联
        systemDao.deleteRolePermissionRelation(id);
        // 删除角色
        systemDao.deleteRole(id);
        return ResponseJson.success();
    }

    /**
     * 角色授权菜单
     */
    @Override
    public ResponseJson<Void> setRoleMenu(Integer id, SysRole sysRole) {
        // 删除原有角色菜单关联
        systemDao.deleteRoleMenuRelation(id);
        // 保存角色菜单关系
        String menuIds = sysRole.getMenuIds();
        saveRoleMenuRelation(id, menuIds);
        return ResponseJson.success();
    }

    /**
     * 保存角色菜单关系
     */
    private void saveRoleMenuRelation(Integer roleId, String menuIds) {
        if (StringUtils.hasLength(menuIds)) {
            // 删除原有角色菜单关联
            systemDao.deleteRoleMenuRelation(roleId);
            // 删除原有角色权限关联
            systemDao.deleteRolePermissionRelation(roleId);
            // 设置角色菜单
            String[] split = menuIds.split(",");
            for (String s : split) {
                if (StringUtils.hasLength(s)) {
                    if (s.contains("-")) {
                        // 保存角色权限关系
                        String[] ids = s.split("-");
                        systemDao.insertRolePermissionRelation(roleId, Integer.valueOf(ids[0]));
                    } else {
                        // 保存角色菜单关系
                        systemDao.insertRoleMenuRelation(roleId, Integer.valueOf(s));
                    }
                }
            }
        }
    }
}
