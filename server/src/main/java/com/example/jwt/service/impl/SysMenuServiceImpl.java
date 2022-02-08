package com.example.jwt.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.jwt.dao.SystemDao;
import com.example.jwt.entity.*;
import com.example.jwt.service.SysMenuService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * Description
 *
 * @author : Charles
 * @date : 2022/1/6
 */
@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SystemDao systemDao;

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
        List<SysMenu> menus = systemDao.getMenuList(parentId, status);
        PaginationVo<SysMenu> pageData = new PaginationVo<>(menus);
        return ResponseJson.success(pageData);
    }

    /**
     * 获取所有启用的菜单列表（树状结构返回）
     */
    @Override
    public ResponseJson<List<SysMenuTree>> getMenuTree() {
        List<SysMenuTree> menus = systemDao.getMenuTree("0");
        // 递归设置子菜单
        setSubMenusData(menus);
        return ResponseJson.success(menus);
    }

    /**
     * 递归设置子菜单（树状结构）
     */
    private void setSubMenusData(List<SysMenuTree> menus) {
        if (!CollectionUtils.isEmpty(menus)) {
            for (SysMenuTree menu : menus) {
                // 获取子菜单
                List<SysMenuTree> submenus = systemDao.getMenuTree(menu.getId());
                if (!CollectionUtils.isEmpty(submenus)) {
                    setSubMenusData(submenus);
                    menu.setSubMenus(submenus);
                } else {
                    // 获取当前菜单的按钮权限
                    List<SysMenuTree> permissions = systemDao.getTreePermissions(menu.getId());
                    if (!CollectionUtils.isEmpty(permissions)) {
                        menu.setSubMenus(permissions);
                    }
                }
            }
        }
    }

    /**
     * 根据ID获取菜单
     */
    @Override
    public ResponseJson<SysMenu> getMenu(Integer id) {
        SysMenu menu = systemDao.getMenu(id);
        // 获取按钮权限
        List<SysPermission> permissions = systemDao.getPermissionList(menu.getId());
        if (!CollectionUtils.isEmpty(permissions)) {
            menu.setPermissions(permissions);
        }
        return ResponseJson.success(menu);
    }

    /**
     * 根据ID更新菜单
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> updateMenu(Integer id, SysMenu menu) {
        menu.setId(id);
        menu.setDelFlag(0);
        systemDao.updateMenu(menu);
        // 按钮权限JSON数据解析
        ResponseJson<Void> error = parsePermissionJson(menu.getId(), menu.getPermissionJson());
        if (error != null) {
            return error;
        }
        return ResponseJson.success();
    }

    /**
     * 根据ID(可选择的)更新菜单字段
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> updateMenuSelective(Integer id, SysMenu menu) {
        SysMenu dbMenu = systemDao.getMenu(id);
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
        systemDao.updateMenu(dbMenu);
        // 按钮权限JSON数据解析
        ResponseJson<Void> error = parsePermissionJson(menu.getId(), menu.getPermissionJson());
        if (error != null) {
            return error;
        }
        return ResponseJson.success();
    }

    /**
     * 添加菜单
     */
    @Transactional(rollbackFor=Exception.class)
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
        SysMenu dbMenu = systemDao.getMenuByName(menu.getName());
        if (null == dbMenu) {
            systemDao.insertMenu(menu);
            // 按钮权限JSON数据解析
            ResponseJson<Void> error = parsePermissionJson(menu.getId(), menu.getPermissionJson());
            if (error != null) {
                return error;
            }
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
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseJson<Void> deleteMenu(Integer id) {
        systemDao.deleteMenu(id);
        List<Integer> permissionIds = systemDao.getPermissionIds(id);
        // 删除该菜单权限按钮
        for (Integer permissionId : permissionIds) {
           systemDao.deletePermission(permissionId);
        }
        return ResponseJson.success();
    }

    /**
     * 按钮权限JSON数据解析
     * @param menuId 菜单Id
     * @param jsonStr 按钮权限JSON串
     */
    private ResponseJson<Void> parsePermissionJson(Integer menuId, String jsonStr) {
        // 按钮权限JSON数据解析
        JSONArray permissionJson = null;
        List<SysPermission> permissionList = new ArrayList<>();
        try {
            permissionJson = parseArray(jsonStr);
            if (null == permissionJson || permissionJson.isEmpty()) {
                return ResponseJson.error("按钮权限JSON数据异常！", null);
            }
            for (Object permissionObject : permissionJson) {
                JSONObject detail = (JSONObject) permissionObject;
                Integer permissionId = (Integer) detail.get("id");
                String permissionName = (String) detail.get("name");
                String permissionTitle = (String) detail.get("title");
                SysPermission item = new SysPermission();
                item.setId(permissionId);
                item.setMenuId(menuId);
                item.setName(permissionName);
                item.setTitle(permissionTitle);
                permissionList.add(item);
            }
        } catch (Exception e) {
            log.error("按钮权限JSON数据解析异常try-catch:", e);
            return ResponseJson.error("按钮权限JSON数据解析异常！", null);
        }
        List<Integer> dbPermissionIds = systemDao.getPermissionIds(menuId);
        List<Integer> permissionIds = new ArrayList<>();
        for (SysPermission permission : permissionList) {
            if (null != permission.getId() && permission.getId() > 0 && dbPermissionIds.contains(permission.getId())) {
                systemDao.updatePermission(permission);
                permissionIds.add(permission.getId());
            } else {
                systemDao.insertPermission(permission);
            }
        }
        for (Integer id : dbPermissionIds) {
            if (!permissionIds.contains(id)){
                systemDao.deletePermission(id);
            }
        }
        return null;
    }



}
