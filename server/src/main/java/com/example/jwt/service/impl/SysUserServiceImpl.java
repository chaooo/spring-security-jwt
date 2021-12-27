package com.example.jwt.service.impl;

import com.example.jwt.dao.SysUserDao;
import com.example.jwt.entity.*;
import com.example.jwt.service.RedisService;
import com.example.jwt.service.SysUserService;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description
 *
 * @author : Charles
 * @date : 2021/12/10
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private RedisService redisService;

    /**
     * 获取用户信息
     */
    @Override
    public ResponseJson<SysUser> getInfoByUsername(String username) {
        if (StringUtils.hasLength(username)) {
            SysUser sysUser = sysUserDao.findByUsername(username);
            // 获取用户角色列表
            List<String> roleList = sysUserDao.getRoleListByUserId(sysUser.getId());
            sysUser.setRoles(roleList);

            // 获取用户角色Id
            List<Integer> roleIds = sysUserDao.getRoleIdsByUsername(username);
            List<SysMenu> menus = null;
            if (!CollectionUtils.isEmpty(roleIds)) {
                // 根据角色Id获取菜单列表
                menus = sysUserDao.getMenusByRoleIds(roleIds);
            }
            sysUser.setMenus(menus);
            sysUser.setPassword("");
            return ResponseJson.success(sysUser);
        }
        return ResponseJson.error("用户数据异常", null);
    }

    /**
     * 退出登录
     *
     * @param token
     */
    @Override
    public ResponseJson<Void> logout(String token) {
        if (StringUtils.hasLength(token)) {
            redisService.remove(token);
        }
        return ResponseJson.success();
    }

    /**
     * 获取用户列表
     *
     * @param username 用户名
     * @param fullName 姓名
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseJson<PaginationVo<SysUser>> getUserList(String username, String fullName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> menus = sysUserDao.getUserList(username, fullName);
        PaginationVo<SysUser> pageData = new PaginationVo<>(menus);
        return ResponseJson.success(pageData);
    }

    /**
     * 根据ID获取用户
     */
    @Override
    public ResponseJson<SysUser> getUser(Integer id) {
        SysUser sysUser = sysUserDao.getUser(id);
        if (null != sysUser) {
            // 根据用户ID获取角色Ids
            List<String> roleIdList = sysUserDao.getRoleIdsByUserId(id);
            StringBuilder roleIds = new StringBuilder();
            for (int i = 0; i < roleIdList.size(); i++) {
                if (StringUtils.hasLength(roleIdList.get(i))){
                    if (i == 0) {
                        roleIds.append(roleIdList.get(i));
                    } else {
                        roleIds.append(",").append(roleIdList.get(i));
                    }
                }
            }
            sysUser.setRoleIds(roleIds.toString());
        }
        return ResponseJson.success(sysUser);
    }

    /**
     * 根据ID更新用户
     */
    @Override
    public ResponseJson<Void> updateUser(Integer id, SysUser sysUser) {
        sysUser.setId(id);
        sysUser.setDelFlag(0);
        if (StringUtils.hasLength(sysUser.getPassword())){
            // 密码加密
            String encodePassword = passwordEncoder.encode(sysUser.getPassword());
            sysUser.setPassword(encodePassword);
        }
        // 删除原有角色关联
        sysUserDao.deleteUserRoleRelation(id);
        // 前端传入角色Ids，用","隔开
        String roleIds = sysUser.getRoleIds();
        // 保存用户角色关系
        saveRoleRelation(sysUser.getId(), roleIds);
        sysUserDao.updateSysUser(sysUser);
        return ResponseJson.success();
    }

    /**
     * 添加用户
     */
    @Override
    public ResponseJson<Void> addUser(SysUser sysUser) {
        if (StringUtils.hasLength(sysUser.getUsername()) && StringUtils.hasLength(sysUser.getPassword())) {
            // 查询是否存在当前用户名的用户
            SysUser dbUser = sysUserDao.findByUsername(sysUser.getUsername());
            if (null == dbUser || dbUser.getDelFlag() != 0) {
                // 密码加密
                String encodePassword = passwordEncoder.encode(sysUser.getPassword());
                sysUser.setPassword(encodePassword);
                // 新增用户
                sysUserDao.insertSysUser(sysUser);
                // 角色Ids，用","隔开
                String roleIds = sysUser.getRoleIds();
                // 保存用户角色关系
                saveRoleRelation(sysUser.getId(), roleIds);
                return ResponseJson.success("添加用户成功", null);
            } else if (!Integer.valueOf(0).equals(dbUser.getDelFlag())){
                // 更新已删除用户
                return updateUser(dbUser.getId(), sysUser);
            } else {
                return ResponseJson.error("用户名已存在", null);
            }
        }
        return ResponseJson.error("用户名或密码不能为空", null);
    }

    /**
     * 根据ID删除用户
     */
    @Override
    public ResponseJson<Void> deleteUser(Integer id) {
        // 删除用户角色关联
        sysUserDao.deleteUserRoleRelation(id);
        // 删除用户
        sysUserDao.deleteSysUser(id);
        return ResponseJson.success();
    }

    /**
     * 保存用户角色关系
     */
    private void saveRoleRelation(Integer userId, String roleIds) {
        if (StringUtils.hasLength(roleIds)) {
            // 设置用户角色
            String[] split = roleIds.split(",");
            for (String s : split) {
                if (StringUtils.hasLength(s)) {
                    // 保存用户角色关系
                    sysUserDao.insertUserRoleRelation(userId, Integer.valueOf(s));
                }
            }
        }
    }

    /**
     * 获取角色列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseJson<PaginationVo<SysRole>> getRoleList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> roles = sysUserDao.getRoleList();
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
            List<String> menuIdList = sysUserDao.getMenuIdsByRoleId(id);
            StringBuilder menuIds = new StringBuilder();
            for (int i = 0; i < menuIdList.size(); i++) {
                if (StringUtils.hasLength(menuIdList.get(i))){
                    if (i == 0) {
                        menuIds.append(menuIdList.get(i));
                    } else {
                        menuIds.append(",").append(menuIdList.get(i));
                    }
                }
            }
            sysRole.setMenuIds(menuIds.toString());
        }
        return ResponseJson.success(sysRole);
    }

    private SysRole getRole(Integer id) {
        return sysUserDao.getRole(id);
    }

    /**
     * 根据ID更新角色
     */
    @Override
    public ResponseJson<Void> updateRole(Integer id, SysRole sysRole) {
        sysRole.setId(id);
        sysRole.setDelFlag(0);
        // 删除原有角色菜单关联
        sysUserDao.deleteRoleMenuRelation(id);
        // 菜单Ids，用","隔开
        String menuIds = sysRole.getMenuIds();
        sysUserDao.updateRole(sysRole);
        // 保存角色菜单关系
        saveRoleMenuRelation(sysRole.getId(), menuIds);
        return ResponseJson.success();
    }

    /**
     * 添加角色
     */
    @Override
    public ResponseJson<Void> addRole(SysRole sysRole) {
        if (StringUtils.hasLength(sysRole.getRoleName())) {
            // 查询是否存在当前角色
            SysRole dbRole  = sysUserDao.getRoleByRoleName(sysRole.getRoleName());
            if (null == dbRole) {
                String menuIds = sysRole.getMenuIds();
                // 保存角色
                sysUserDao.insertRole(sysRole);
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
    @Override
    public ResponseJson<Void> deleteRole(Integer id) {
        // 删除角色菜单关联
        sysUserDao.deleteRoleMenuRelation(id);
        // 删除角色
        sysUserDao.deleteRole(id);
        return ResponseJson.success();
    }

    /**
     * 角色授权菜单
     */
    @Override
    public ResponseJson<Void> setRoleMenu(Integer id, SysRole sysRole) {
        // 删除原有角色菜单关联
        sysUserDao.deleteRoleMenuRelation(id);
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
            // 设置角色菜单
            String[] split = menuIds.split(",");
            for (String s : split) {
                if (StringUtils.hasLength(s)) {
                    // 保存角色菜单关系
                    sysUserDao.insertRoleMenuRelation(roleId, Integer.valueOf(s));
                }
            }
        }
    }

    /**
     * 获取菜单列表
     *
     * @param parentId 父级菜单Id
     * @param status   状态：0启用，1停用
     * @param pageNum  页码
     * @param pageSize 每页大小
     */
    @Override
    public ResponseJson<PaginationVo<SysMenu>> getMenuList(Integer parentId, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysMenu> menus = sysUserDao.getMenuList(parentId, status);
        PaginationVo<SysMenu> pageData = new PaginationVo<>(menus);
        return ResponseJson.success(pageData);
    }

    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    @Override
    public ResponseJson<List<SysMenu>> getMenuTree() {
        List<SysMenu> menus = sysUserDao.getMenuList(0, 0);
        // 递归设置子菜单
        setSubMenusData(menus);
        return ResponseJson.success(menus);
    }

    /**
     * 递归设置子菜单
     */
    private void setSubMenusData(List<SysMenu> menus) {
        if (!CollectionUtils.isEmpty(menus)) {
            for (SysMenu menu : menus) {
                List<SysMenu> submenus = sysUserDao.getMenuList(menu.getId(), 0);
                if (!CollectionUtils.isEmpty(submenus)) {
                    setSubMenusData(submenus);
                    menu.setSubMenus(submenus);
                }
            }
        }
    }

    /**
     * 根据ID获取菜单
     */
    @Override
    public ResponseJson<SysMenu> getMenu(Integer id) {
        SysMenu menu = sysUserDao.getMenu(id);
        return ResponseJson.success(menu);
    }

    /**
     * 根据ID更新菜单
     */
    @Override
    public ResponseJson<Void> updateMenu(Integer id, SysMenu menu) {
        menu.setId(id);
        menu.setDelFlag(0);
        sysUserDao.updateMenu(menu);
        return ResponseJson.success();
    }

    /**
     * 根据ID(可选择的)更新菜单字段
     */
    @Override
    public ResponseJson<Void> updateMenuSelective(Integer id, SysMenu menu) {
        SysMenu dbMenu = sysUserDao.getMenu(id);
        if (StringUtils.hasLength(menu.getTitle())){
            dbMenu.setTitle(menu.getTitle());
        }
        if (StringUtils.hasLength(menu.getName())){
            dbMenu.setName(menu.getName());
        }
        if (StringUtils.hasLength(menu.getIcon())){
            dbMenu.setIcon(menu.getIcon());
        }
        if (null != menu.getParentId()){
            dbMenu.setParentId(menu.getParentId());
        }
        if (null != menu.getSort()){
            dbMenu.setSort(menu.getSort());
        }
        if (null != menu.getHidden()){
            dbMenu.setHidden(menu.getHidden());
        }
        if (null != menu.getStatus()){
            dbMenu.setStatus(menu.getStatus());
        }
        sysUserDao.updateMenu(dbMenu);
        return ResponseJson.success();
    }

    /**
     * 添加菜单
     *
     * @param menu
     */
    @Override
    public ResponseJson<Void> addMenu(SysMenu menu) {
        if (null == menu.getParentId()) {
            menu.setParentId(0);
        }
        if (!StringUtils.hasLength(menu.getName())) {
            return ResponseJson.error("菜单路由名不能为空！", null);
        }
        if (!StringUtils.hasLength(menu.getTitle())) {
            return ResponseJson.error("菜单名称不能为空！", null);
        }
        // 根据路由名称查询菜单
        SysMenu dbMenu = sysUserDao.getMenuByName(menu.getName());
        if (null == dbMenu) {
            sysUserDao.insertMenu(menu);
        } else if (!Integer.valueOf(0).equals(dbMenu.getDelFlag())){
            // 更新已删除菜单
            return updateMenu(dbMenu.getId(), menu);
        } else {
            return ResponseJson.error("菜单路由名已存在", null);
        }
        return ResponseJson.success();
    }

    /**
     * 根据ID删除菜单
     *
     * @param id
     */
    @Override
    public ResponseJson<Void> deleteMenu(Integer id) {
        sysUserDao.deleteMenu(id);
        return ResponseJson.success();
    }
}
